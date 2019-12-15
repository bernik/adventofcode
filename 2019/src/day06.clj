(ns day6
  (:require [clojure.java.io :refer [resource file]]
            [clojure.string :refer [split-lines]]))

(def input (->> (slurp (file (resource "day6.txt")))
                split-lines
                (map #(vector (subs % 4) (subs % 0 3)))
                (into {})))


(def test-input {"B" "COM"
                 "C" "B" 
                 "D" "C" 
                 "E" "D" 
                 "F" "E" 
                 "G" "B" 
                 "H" "G" 
                 "I" "D" 
                 "J" "E" 
                 "K" "J" 
                 "L" "K" 
                 "YOU" "K"
                 "SAN" "I" })

(defn orbits [from to] 
  (take-while #(not= to %) (iterate #(input %) from)))

(defn part1 []
  (->> input
       keys
       (map #(count (orbits % "COM")))
       (apply +)))

(defn part2 []
  (let [s1 (set (orbits "YOU" "COM"))
        s2 (set (orbits "SAN" "COM"))]
    (- (+ (count (clojure.set/difference s1 s2))
          (count (clojure.set/difference s2 s1)))
       2)))


(comment
  (part1)
  (part2)
  (with-redefs [input test-input]
    [(part1) (part2)])
  ())


