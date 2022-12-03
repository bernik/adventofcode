(ns adventofcode2022.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :refer [intersection]]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq))


(def test-input (parse-input "day03.test.txt"))
(def input (parse-input "day03.txt"))


(defn priority [c]
  (let [code (int c)]
    (- code (if (>= code 97) 96 38))))


(defn part-1 [input]
  (->> input
       (map #(split-at (quot (count %) 2) %))
       (map #(->> % (map set) (apply (comp first intersection))))
       (map priority)
       (apply +)))


(defn part-2 [input]
  (->> input
       (partition 3)
       (map #(->> % (map set) (apply (comp first intersection))))
       (map priority)
       (apply +)))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))

