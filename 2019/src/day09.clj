(ns day9
  (:require [clojure.java.io :refer [file resource]]
            [clojure.string :refer [split]]
            [intcode-computer :as computer]))

(def input (into [] (map read-string (split (slurp (file (resource "day9.txt"))) #","))))

(computer/run input 0 1)
(computer/run input 0 2)

(comment
  (with-redefs [computer/debug? true]
    ; (computer/run [109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99] 0)
    (computer/run input 0 1)
    ; (computer/run [104,1125899906842624,99] 0)
    ; (computer/run [1102,34915192,34915192,7,4,7,99,0] 0)
    #_())

  #_())