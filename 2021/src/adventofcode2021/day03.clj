(ns adventofcode2021.day03
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(def test-input (line-seq (->> "day03.test.txt" io/resource io/file io/reader)))
(def input (line-seq (->> "day03.txt" io/resource io/file io/reader)))

;; most common bit
(defn mcb [xs i]
  (let [mask (bit-shift-left 1 i)
        ones (->> xs
                  (map #(min 1 (bit-and % mask)))
                  (apply +))]
    (if (> ones (/ (count xs) 2)) 1 0)))

;; less common bit
(defn lcb [xs i] (rem (+ 1 (mcb xs i)) 2))


(defn part-1 [lines]
  (let [numbers (map #(Integer/parseInt % 2) lines)
        gamma   (->> lines first count range reverse (map #(bit-shift-left (mcb numbers %) %)) (apply +))
        epsilon (->> lines first count range reverse (map #(bit-shift-left (lcb numbers %) %)) (apply +))]
    (* gamma epsilon)))


(comment 
  (part-1 test-input)
  (part-1 input)
)
  
