(ns adventofcode.day23
  (:require [clojure.java.io :refer [file resource]]
            [clojure.string :refer [split-lines]]))

(defn parse-instruction [in]
  nil)

(def program (->> (resource (file "day23.txt"))
                  split-lines 
                  (map parse-instruction)))
