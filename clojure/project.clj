(defproject clojure-grpc-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.grpc/grpc-core "1.0.1" ]
                 [io.grpc/grpc-netty "1.0.1" :exclusions [io.grpc/grpc-core io.netty/netty-codec-http2]]
                 [io.netty/netty-codec-http2 "4.1.3.Final" :exclusions [io.grpc/grpc-core]]
                 [io.grpc/grpc-protobuf "1.0.1"]
                 [io.grpc/grpc-stub "1.0.1"]
                 [grpc.transformer "0.1.0-SNAPSHOT"]
                 [grpc.server "0.1.0-SNAPSHOT"]]
  :main ^:skip-aot clojure-grpc-demo
  :target-path "target/%s"
  :java-source-paths ["build/generated/source/proto/main/grpc"
                      "build/generated/source/proto/main/java"]
  :profiles {:uberjar {:aot :all}})
