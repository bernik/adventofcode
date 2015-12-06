(ns adventofcode.day6
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]))

(def grid (into {} (for [x (range 0 1000) y (range 0 1000)] [[x y] 0])))

(defn on-off [command]
  (case command
    "turn on" (fn [n] 1)
    "turn off" (fn [n] 0)
    "toggle" (fn [n] (if (= n 1) 0 1))))

(defn inc-dec-brightness [command]
  (case command
    "turn on" (fn [n] (inc n))
    "turn off" (fn [n] (if (> n 0) 
                         (dec n) 
                         0))
    "toggle" (fn [n] (+ n 2))))

(defn apply-line [grid line command->fn]
  (let [[_ command-str x1 y1 x2 y2] (re-find #"(.*) (\d+),(\d+) through (\d+),(\d+)" line)
        update-fn (command->fn command-str)
        affected-cells (for [x (range (read-string x1) (inc (read-string x2))) 
                             y (range (read-string y1) (inc (read-string y2)))] 
                         [x y])]
    (reduce #(update %1 %2 update-fn) grid affected-cells)))

(def input-seq (clojure.string/split-lines (slurp (io/file (io/resource "day6.txt")))))

(defn part-one [] 
  (->> input-seq
       (reduce #(apply-line %1 %2 on-off) grid)
       (filter #(= 1 (second %)))
       count))

(defn part-two []
  (->> input-seq
       (reduce #(apply-line %1 %2 inc-dec-brightness) grid)
       vals
       (apply + )))
