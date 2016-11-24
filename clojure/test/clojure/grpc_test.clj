(ns clojure.grpc-test
  (:require [clojure.test :refer :all]
            [clojure.grpc :refer :all])
  (:import [michael Demo$DemoMessage Demo$M1]))









(def clz Demo$DemoMessage)


(def oo {:str "aaa"
         :index 12
         :foo "VALUE_C"
         :bar "BAR_B"
         :m2 {:s "hello" :i 8888}
         :m_list [{:s "hello" :i 3} {:s "hello" :i 2} {:s "hello" :i 1}]})

(def builder (.invoke (find-builder clz) nil nil))
(def descriptor (call-static-class-method clz "getDescriptor"))
(def xx (.getFields descriptor))



;;; "getMListList" "getMListOrBuilderList" "getMListCount" "getMList" "getMListOrBuilder

;;; ("ensureMListIsMutable" "setMList" "setMList" "addMList" "addMList" "addMList" "addMList" "addAllMList" "clearMList" "removeMList" "getMListBuilder" "addMListBuilder" "addMListBuilder" "getMListBuilderList" "getMListFieldBuilder" "getMListList" "getMListOrBuilderList" "getMListCount" "getMList" "getMListOrBuilder")



(def repm (->message {:s "hello" :i 8888} Demo$M1))
(def repm2 (->message {:s "hello111" :i 888800} Demo$M1))


;;


(deftest ->message-test
  (testing "test clojure message transfer to protobuf message "
    (is (nil? (prn     (->message oo clz))))
    )

  )


(comment
  (def mm
    (->message oo clz)

    (def m

      (find-method (class builder)
                   "get"
                   (get type-map type))

      )


    (into [] (.getDeclaredMethods (first (into [] (.getParameterTypes xx)))))

    (filter #(contains? #{"addRepeatedField" "getDescriptorForType"} (.getName %) ) (into [] (.getDeclaredMethods mmm1)))
    (build :MESSAGE builder )
    (def m1 (first (filter #(= "getDescriptor" (.getName %) ) (into [] (.getDeclaredMethods mmm1)))))

    ))
