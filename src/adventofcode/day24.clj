(ns adventofcode.day24
  (:require [clojure.math.combinatorics :as combo]))

(def presents #{1 2 3 5 7 13 17 19 23 29 31 37 41 43 53 59 61 67 71 73 79 83 89 97 101 103 107 109 113})

#_(def part-one (->> (combo/partitions presents :min 3 :max 3)
                     count
                     #_(filter #(and 
                                 (every? (fn [coll] (< 1 (count coll))) %)
                                 (apply = (map (partial apply +) %))))))

(comment (filter #(->> % 
                       (map (partial apply +))
                       (apply =)) 
                 [[[1] [2] [3]]
                  [[1 2] [0 3] [3]]])

         (count (take-while #(< (count %) 4) (combo/subsets (into [] presents)))))
