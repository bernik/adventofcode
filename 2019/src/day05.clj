(ns day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [intcode-computer :as computer]))

(def input (into [] (map read-string (str/split (slurp (io/file (io/resource "day5.txt"))) #","))))

;; part 1
(computer/run input 1)
;; part 2
(computer/run input 5)
