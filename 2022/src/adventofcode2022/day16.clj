(ns adventofcode2022.day16
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map 
      (fn [line]
        (let [[_ from rate valves] (re-matches #".*([A-Z]{2}).*(\d+).*valves? (.*)" line)
              to (re-seq #"[A-Z]{2}" valves)]
          [from {:rate (parse-long rate) :to to}])))
    (into {})))


(def test-input (parse-input "day16.test.txt"))
(def input (parse-input "day16.txt"))






