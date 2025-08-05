(ns mitsubishi-il-simulator.test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [mitsubishi-il-simulator.parser :as parser]
            [mitsubishi-il-simulator.simulator :as sim]))

(deftest test-parser
  (testing "Simple LD instruction parsing"
    (let [result (parser/parse-il "LD X0")]
      (is (not (:error result)))
      (is (vector? (:success result)))))

  (testing "Simple program parsing"
    (let [result (parser/parse-il "LD X0\nOUT Y0")]
      (is (not (:error result)))
      (is (vector? (:success result)))))

  (testing "Complex logic parsing"
    (let [result (parser/parse-il "LD X0\nAND X1\nORB\nOUT Y0")]
      (println "Testing complex logic parsing:")
      (println "Parsing\nLD X0\nAND X1\nORB\nOUT Y0\nresult:")
      (pprint result)
      (is (not (:error result)))
      (is (vector? (:success result))))))

(deftest test-simulator
  (testing "Device value operations"
    (sim/reset-plc-state!)
    (sim/set-device-value! :input 0 true)
    (is (= true (sim/get-device-value :input 0)))
    (sim/set-device-value! :output 0 false)
    (is (= false (sim/get-device-value :output 0))))

  (testing "Input toggle"
    (sim/reset-plc-state!)
    (sim/toggle-input 0)
    (is (= true (sim/get-device-value :input 0)))
    (sim/toggle-input 0)
    (is (= false (sim/get-device-value :input 0))))

  (testing "Simple program execution"
    (sim/reset-plc-state!)
    (sim/set-input 0 true)
    (let [result (sim/load-program "LD X0\nOUT Y0")]
      (is (:success result))
      (sim/start-execution)
      (sim/step-execution) ; Execute LD X0
      (sim/step-execution) ; Skip empty line
      (sim/step-execution) ; Execute OUT Y0
      (is (= true (sim/get-output 0))))))

(deftest test-logical-operations
  (testing "AND operation"
    (sim/reset-plc-state!)
    (sim/set-input 0 true)
    (sim/set-input 1 true)
    (sim/load-program "LD X0\nAND X1\nOUT Y0")
    (sim/start-execution)
    (sim/step-execution) ; LD X0
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; AND X1  
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; OUT Y0
    (is (= true (sim/get-output 0))))

  (testing "OR operation"
    (sim/reset-plc-state!)
    (sim/set-input 0 true)
    (sim/set-input 1 false)
    (sim/load-program "LD X0\nOR X1\nOUT Y0")
    (sim/start-execution)
    (sim/step-execution) ; LD X0
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; OR X1
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; OUT Y0
    (is (= true (sim/get-output 0))))

  (testing "SET/RST operations"
    (sim/reset-plc-state!)
    (sim/set-input 0 true)
    (sim/load-program "LD X0\nSET M0\nLD M0\nOUT Y0")
    (sim/start-execution)
    (sim/step-execution) ; LD X0
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; SET M0
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; LD M0
    (sim/step-execution) ; Skip empty
    (sim/step-execution) ; OUT Y0
    (is (= true (sim/get-output 0)))))

(run-tests)
