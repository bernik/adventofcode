(ns day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (into [] (map read-string (str/split (slurp (io/file (io/resource "day5.txt"))) #","))))
