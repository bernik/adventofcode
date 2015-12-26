(ns adventofcode.day24
  (:require [clojure.math.combinatorics :as combo]))

(def presents [1 2 3 5 7 13 17 19 23 29 31 37 41 43 53 59 61 67 71 73 79 83 89 97 101 103 107 109 113])
(def presents-count (count presents))
(def total-weight (apply + presents))

(combo/combinations presents 4)
(defn min-qe [parts n] 
  (let [qes (->> (combo/combinations presents n)  
                 (filter #(= (apply + %) (/ total-weight parts)))
                 (pmap #(apply * %)))]  
    (when-not (empty? qes) 
      (apply min qes))))

(defn answer [parts] 
  (loop [n 0]
    (if-let [qe (min-qe parts n)]
      qe
      (recur (inc n)))))

(def part-one (answer 3))
(def part-two (answer 4))
