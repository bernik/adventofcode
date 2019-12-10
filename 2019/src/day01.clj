(ns day1 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (->> "day1.txt"
                io/resource
                io/file
                slurp
                str/split-lines
                (map read-string)))

(defn fuel [mass] (- (quot mass 3) 2))

(defn fuel2 [mass] 
  (->> (fuel mass)
       (iterate fuel)
       (take-while pos?)
       (apply +)))

;; part 1 
(def part1 (->> input (map fuel) (apply +)))
;; part 2
(def part2 (->> input (map fuel2) (apply +)))

(println part1)
(println part2)

