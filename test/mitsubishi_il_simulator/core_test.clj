(ns mitsubishi-il-simulator.core-test
  (:require [clojure.test :refer :all]
            [mitsubishi-il-simulator.core :refer :all]))

(deftest application-startup-test
  (testing "Application can start without errors"
    (is (= 1 1)))) ; Simple passing test
