(ns adventofcode2022.day04
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (map #(->> (re-seq #"\d+" %) 
                  (map read-string)))))


(def test-input (parse-input "day04.test.txt"))
(def input (parse-input "day04.txt"))


(defn part-1 [input]
  (->> input
       (filter (fn [[a1 b1 a2 b2]]
                 (or (and (<= a1 a2)
                          (<= b2 b1))
                     (and (<= a2 a1)
                          (<= b1 b2)))))
       count))


(defn part-2 [input]
  (->> input
       (filter (fn [[a1 b1 a2 b2]]
                 (and (<= a1 b2)
                      (<= a2 b1))))
       count))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input)
  )

