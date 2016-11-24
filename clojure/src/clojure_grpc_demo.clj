(ns clojure-grpc-demo
  (:require [grpc.transformer :refer [->message]])
  (:import [io.grpc Server ServerBuilder]
           [michael DemoServiceGrpc DemoServiceGrpc$DemoServiceImplBase]
           [michael Demo$HelloReply Demo$DemoMessage]
           [michael Demo$DemoMessage$Foo])
  ;;(:gen-class)
  )


(defn <-message []
  )



(defmacro defrpc [name interfaces]
  `(do
     (defmacro ~(symbol (str "defn-" name)) [pname# [request# response#] ~'& body#]
       (let [mname# ~(str name)]
         `(let []
            (update-proxy ~(symbol mname#) {~(str pname#) (fn [~request# ~response#]
                                                            ~@body#)})
            )))
     (def ~name
       (proxy [~@interfaces] []))))

(defrpc demorpc [DemoServiceGrpc$DemoServiceImplBase])


(defn-demorpc sayHello [request response]
  (prn (<-message request))
  (prn "hello word")
  (let [m (->message {:reply "hi savior"}  Demo$HelloReply )]
    (.onNext response m)
    (.onCompleted response)))




(defn start []
  (let [impl demorpc
        server (->(ServerBuilder/forPort 9999)
                  (.addService impl)
                  (.build)
                  (.start)
                  (.awaitTermination))]
    server))




(defn -main [& args]
  (prn "begin to start server")
  (start))
