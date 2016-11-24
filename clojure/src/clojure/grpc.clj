(ns clojure.grpc
  (:import [io.grpc Server ServerBuilder]
           [michael DemoServiceGrpc DemoServiceGrpc$DemoServiceImplBase]
           [michael Demo$HelloReply Demo$DemoMessage]
           [michael Demo$DemoMessage$Foo])
  ;;(:gen-class)
  )

(defn find-method [clz mname & params]
  (first (filter #(and (= mname (.getName %))
                       (= (count params) (count (.getParameterTypes %))))
                 (.getDeclaredMethods clz))))


(defn call-static-class-method [clz mname]
  (let [method (find-method clz mname)]
    (.invoke method nil nil)))

(defn find-builder [clz]
  (find-method  clz "newBuilder" ))

(defn ->Enum [clz val]
  (when-let [field (.getField clz val)]
    (.get field nil)))



(defn ->camelName [name]
  (let [words (clojure.string/split name #"_")]
    (clojure.string/join (map clojure.string/capitalize words))))


(defmulti build (fn [type builder name] type))

(def type-map {:BOOLEAN java.lang.Boolean
               :DOUBLE java.lang.Double
               :ENUM java.lang.Enum
               :FLOAT java.lang.Float
               :INT java.lang.Integer
               :LONG java.lang.Long
               :STRING java.lang.String
;;;BYTE_STRING

               })

(declare ->message-builder)

(defmethod build :STRING [type builder method]
  (fn [val] (when val (.invoke method builder (into-array (get type-map type) [val])))))

(defmethod build :INT [type builder method]
  (fn [val] (when val (.invoke method builder (into-array (get type-map type) [(new Integer val)])))))

(defmethod build :LONG [type builder method]
  (fn [val] (when val (.invoke method builder (into-array (get type-map type) [val])))))



(defmethod build :ENUM [type builder method]
  (let [clz (first (into [] (.getParameterTypes method)))]
    (fn [val] (when val (.invoke method builder (into-array (get type-map type) [(->Enum clz val)]))))))


(defmethod build :MESSAGE [type builder m]
  (let [clazz (first (into [] (.getParameterTypes m)))]
    (fn [val]
      (when val
        (if (sequential? val)
          (let [clazz (Class/forName (clojure.string/replace (.getName clazz) "$Builder" ""))]
            (doseq [v val]
              (let [inner-builder (->message-builder v clazz)
                    message (.build inner-builder)]
                (.invoke m builder (into-array clazz [message])))))
          (let [inner-builder (->message-builder val clazz)
                message (.build inner-builder)]
            (.invoke m builder (into-array clazz [message]))))))))


(defmethod build :default [type builder method]
  (fn [val] (prn val)))

(defn parse-descriptor [descriptor]
  [(keyword (str (.getJavaType descriptor)))
   (.getName descriptor)
   (if (.isRepeated descriptor)
     (str "add" (->camelName (.getName descriptor)))
     (str "set" (->camelName (.getName descriptor))))
   (.isRepeated descriptor)
   (str "get" (->camelName (.getName descriptor)) (when (.isRepeated descriptor) "List"))
   (.getJavaType descriptor)
   (.getType descriptor)])




(defn ->message-builder [o clz]
  (let [builder (.invoke (find-builder clz) nil nil)
        descriptor (call-static-class-method clz "getDescriptor")]
    (doseq [field (.getFields descriptor)]
      (let [[type name mname repeated & rest] (parse-descriptor field)
            field-builder (build type builder (find-method (class builder)
                                                           mname
                                                           (get type-map type)))]
        (field-builder (o (keyword name)))))
    builder))



(defn ->message [o clz]
  (.build (->message-builder o clz)))

(defn <-message [m]
  (let [clz (class m)
        descriptor (call-static-class-method clz "getDescriptor")]
    (reduce merge (for [field (.getFields descriptor)]
                    (let [[type name _ repeated gname & rest] (parse-descriptor field)]
                      (cond repeated
                            {(keyword name) (into [] (map <-message (.invoke (find-method  clz gname) m nil)))}
                            (= :ENUM type)
                            {(keyword name) (str (.invoke (find-method  clz gname) m nil))}
                            (= :MESSAGE type)
                            {(keyword name) (<-message (.invoke (find-method  clz gname) m nil))}
                            :default
                            {(keyword name) (.invoke (find-method  clz gname) m nil)}
                            ))))))


(defn start []
  (let [impl (proxy [DemoServiceGrpc$DemoServiceImplBase] []
               (sayHello [request response]
                 (prn (<-message request))
                 (prn "hello word")
                 (let [m (->message {:reply "hi savior"}  Demo$HelloReply )]
                   (.onNext response m)
                   (.onCompleted response)
                   )
                 ))
        server (->(ServerBuilder/forPort 9999)
                  (.addService impl)
                  (.build)
                  (.start)
                  (.awaitTermination))]
    server))




(defn -main [& args]
  (prn "begin to start server")
  (start)
  )
