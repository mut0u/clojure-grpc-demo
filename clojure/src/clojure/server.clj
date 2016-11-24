(ns clojure.server
  (:import [io.grpc Server ServerBuilder]
           [michael DemoServiceGrpc DemoServiceGrpc$DemoServiceImplBase]
           [michael Demo$HelloReply Demo$DemoMessage]))
