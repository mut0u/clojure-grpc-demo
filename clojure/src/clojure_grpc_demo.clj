(ns clojure-grpc-demo
  (:require [grpc.transformer :refer [->message <-message]]
            [grpc.server :refer [defrpc]])
  (:import [io.grpc Server ServerBuilder]
           [michael DemoServiceGrpc DemoServiceGrpc$DemoServiceImplBase]
           [michael Demo$HelloReply Demo$DemoMessage]
           [michael Demo$DemoMessage$Foo])
  ;;(:gen-class)
  )



(defrpc demorpc [DemoServiceGrpc$DemoServiceImplBase])

(defn build-response [msg clz response]
  (some->> (->message msg clz)
           (.onNext response)))

(defn-demorpc sayHello [r p]
  (prn "the message " r)
  (build-response {:reply "hi savior"}  Demo$HelloReply  p))



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
