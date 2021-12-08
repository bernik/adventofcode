(ns adventofcode2021.day07
  (:require [clojure.java.io :as io]))

(def test-input (read-string "[16,1,2,0,4,2,7,1,2,14]"))
(def input (read-string (slurp (io/resource "day07.edn"))))

(defn abs [n] (max n (- n)))

(defn part-1 [xs]
  (->> (range (apply min xs) (inc (apply max xs)))
       (map (fn [n] [n (->> xs (map #(abs (- % n))) (apply +))]))
       (sort-by second)
       first
       second))

(defn part-2 [xs]
  (->> (range (apply min xs) (inc (apply max xs)))
       (map (fn [n]
              [n (->> xs
                      (map #(quot (* (abs (- % n)) (inc (abs (- % n)))) 2))
                      (apply +))]))
       (sort-by second)
       first
       second))

(comment
  (part-1 test-input)
  (part-1 input)

  (part-2 test-input)
  (part-2 input))