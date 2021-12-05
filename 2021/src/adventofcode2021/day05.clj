(ns adventofcode2021.day04
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))


(defn parse-input [path]
  (->> (io/reader (io/resource path))
       line-seq
       (map #(->> %
                  (re-matches #"(\d+),(\d+) -> (\d+),(\d+)")
                  rest
                  (map read-string)))))

(def test-input (parse-input "day05.test.txt"))
(def input (parse-input "day05.txt"))

(defn abs [n] (max n (- n)))

(defn line [x1 y1 x2 y2]
  (map vector
       (cond
         (= x1 x2) (repeat x1)
         (< x1 x2) (range x1 (inc x2))
         :else (range x1 (dec x2) -1))
       (cond
         (= y1 y2) (repeat y1)
         (< y1 y2) (range y1 (inc y2))
         :else (range y1 (dec y2) -1))))


(defn overlap-points [diagonals? input]
  (->> input
       (filter (fn [[x1 y1 x2 y2]]
                 (or (and diagonals?
                          (= (abs (- x1 x2))
                             (abs (- y1 y2))))
                     (= x1 x2) 
                     (= y1 y2))))
       (mapcat #(apply line %))
       frequencies
       (filter #(< 1 (second %)))
       count))


(def part-1 (partial overlap-points false)) 
(def part-2 (partial overlap-points true))

(defn diagram [points]
  (let [cols (inc (apply max (map first points)))
        rows (inc (apply max (map second points)))
        row (into [] (take cols (repeat 0)))
        grid (into [] (take rows (repeat row)))]
    (reduce #(update-in %1 (reverse %2) inc) grid points)))

(comment
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))
  
