(ns adventofcode2021.day03
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(def test-input (line-seq (->> "day03.test.txt" io/resource io/file io/reader)))
(def input      (line-seq (->> "day03.txt"      io/resource io/file io/reader)))

(defn to-bin-str [size n]
  (clojure.string/replace (format (str "%" size "s") (Integer/toBinaryString n)) " " "0"))


;; most common bit
(defn mcb [xs i]
  (let [mask (bit-shift-left 1 i)
        ones (->> xs
                  (map #(min 1 (bit-and % mask)))
                  (apply +))]
    (if (>= ones (/ (count xs) 2)) 1 0)))

;; less common bit
(defn lcb [xs i] (rem (+ 1 (mcb xs i)) 2))


(defn part-1 [lines]
  (let [numbers (map #(Integer/parseInt % 2) lines)
        size (count (first lines))
        gamma   (->> (range size) (map #(bit-shift-left (mcb numbers %) %)) (apply +))
        epsilon (->> (range size) (map #(bit-shift-left (lcb numbers %) %)) (apply +))]
    (* gamma epsilon)))


(defn rating [crit-fn lines]
  (loop [numbers (map #(Integer/parseInt % 2) lines)
         i (dec (count (first lines)))]
    (if (or (neg? i) (= 1 (count numbers)))
      (first numbers)
      (let [criteria (crit-fn numbers i)]
        (recur (filter #(= criteria (bit-and 1 (bit-shift-right % i))) numbers)
               (dec i))))))

(defn part-2 [lines]
  (let [oxygen (rating mcb lines)
        co2 (rating lcb lines)]
    (* oxygen co2)))

(comment 
  (part-1 test-input)
  (part-1 input)

  (part-2 test-input)

  (part-2 input)

  (rating lcb test-input))
