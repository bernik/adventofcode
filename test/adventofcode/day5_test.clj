(ns adventofcode.day5-test
  (:require [clojure.test :refer :all]
            [adventofcode.day5 :refer :all]))

(deftest nice-test 
  (are [s res] (= (nice? s) res)
    "ugknbfddgicrmopn" true
    "aaa" true
    "jchzalrnumimnmhp" false
    "haegwjzuvuyypxyu" false
    "dvszwmarrgswjxmb" false))
