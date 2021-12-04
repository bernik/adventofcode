(ns adventofcode2021.day01
  (:require [clojure.java.io :as io]))

(def test-seq [199 200 208 210 200 207 240 269 260 263])
(def in-seq (map read-string (line-seq (io/reader (io/file (io/resource "day01.txt"))))))

(defn part-1 [input]
  (->> input
       (partition 2 1)
       (filter #(apply < %))
       count))

(defn part-2 [input]
  (->> input
       (partition 3 1)
       (map #(apply + %))
       part-1))


(comment
  (part-1 test-seq)
  (part-1 in-seq)
)