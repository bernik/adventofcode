(ns day23
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [3 8 9 1 2 5 4 6 7])
(def input      [9 5 2 3 1 6 4 8 7])

(defn move [[x1 x2 x3 x4 & xs]]
  (let [sorted (sort > xs)
        dest (first (concat (filter #(< % x1) sorted) sorted))]
    (concat (mapcat #(if (= % dest) 
                       [% x2 x3 x4] 
                       [%]) 
                    xs) 
            [x1])))

;; (pprint (move test-input))

(defn part-1 [input]
  (->> input
       (iterate move)
       (drop 100)
       first))

;; (pprint (part-1 input))

(defn part-2 [input]
  ;; (->> (concat input (range 10 1000001))
  (->> (concat input (range 10 20))
       (iterate move)
       #_(drop 10000000)
       (take 100)
       #_first))

(pprint (part-2 test-input))

