(ns day09
  (:require [clojure.java.io :as io]))

(def test-input [35 20 15 25 47 
                 40 62 55 65 95
                 102 117 150 182
                 127 219 299 277
                 309 576])

(def input (->> "day09.txt"
                io/resource
                io/reader
                line-seq
                (mapv read-string)))


;; part 1 
(defn sums [numbers]
 (loop [[x & xs] numbers
        res []]
   (if-not xs
     res
     (recur xs (into res (map #(+ x %) xs))))))

(defn part-1 [input preamble-size]
  (->> input
       (partition (inc preamble-size) 1)
       (filter (complement
                (fn [xs]
                  (let [[preamble [n]] (split-at (dec (count xs)) xs)]
                    (->> preamble
                         sums
                         (some #(= n %)))))))
       first
       last))

(println (str "part 1: " (part-1 test-input 5)))
(println (str "part 1: " (part-1 input 25)))

;; part 2
(defn part-2 [input preamble-size]
  (let [n (part-1 input preamble-size)]
    (loop [xs input]
      (if-let [xs' (reduce (fn [[coll sum] x]
                             (cond
                               (= n sum) (reduced coll)
                               (< n sum) (reduced nil)
                               :else
                               [(conj coll x) (+ sum x)]))
                           [[] 0]
                           xs) ]
        (+ (apply max xs') (apply min xs'))
        (recur (rest xs))))))

(println (part-2 test-input 5))
(println (part-2 input 25))