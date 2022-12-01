(ns adventofcode2022.day01
  (:require [clojure.java.io :as io]))

(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map #(read-string (if (= "" %) "nil" %)))
    (partition-by #(nil? %))
    (filter #(not= '(nil) %))
    (map #(apply + %))))

(def test-input (parse-input "day01.test.txt"))
(def input (parse-input "day01.txt"))

(def part-1 (partial apply max))

(defn part-2 [input]
  (->> input
      sort
      (take-last 3)
      (apply +)))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input) 
  )

