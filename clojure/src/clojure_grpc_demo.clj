(ns clojure-grpc-demo
  (:require [grpc.transformer :refer [->message <-message]])
  (:import [io.grpc Server ServerBuilder]
           [michael DemoServiceGrpc DemoServiceGrpc$DemoServiceImplBase]
           [michael Demo$HelloReply Demo$DemoMessage]
           [michael Demo$DemoMessage$Foo])
  ;;(:gen-class)
  )




(defmacro defrpc [name interfaces]
  `(do
     (defmacro ~(symbol (str "defn-" name)) [pname# params# ~'& body#]
       (let [mname# ~(str name)]
         `(let []
            (update-proxy ~(symbol mname#) {~(str pname#) (fn [~'~'this ~@params#]
                                                            ~@body#)})
            )))
     (def ~name
       (proxy [~@interfaces] []))))

(defrpc demorpc [DemoServiceGrpc$DemoServiceImplBase])


(defn-demorpc sayHello [r p]
  (prn (<-message r))
  (prn "hello word")
  (let [m (->message {:reply "hi savior"}  Demo$HelloReply )]
    (.onNext p m)
    (.onCompleted p)))



(defn start []
  (let [server (->(ServerBuilder/forPort 9999)
                  (.addService demorpc)
                  (.build)
                  (.start)
                  (.awaitTermination))]
    server))


(defn -main [& args]
  (prn "begin to start server")
  (start))
