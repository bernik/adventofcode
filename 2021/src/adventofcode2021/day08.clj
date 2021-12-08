(ns adventofcode2021.day08
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :refer [intersection]]))

(defn parse-line [line]
  (let [[left rigth] (str/split line #" \| ")]
    [(str/split left #" ") (str/split rigth #" ")]))

(def test-input (map parse-line (line-seq (io/reader (io/resource "day08.test.txt")))))
(def input (map parse-line (line-seq (io/reader (io/resource "day08.txt")))))


(defn part-1 [input]
  (->> input
       (mapcat second)
       (filter #(#{2 3 4 7} (count %)))
       count))

;; 1 = count 2
;; 7 = count 3
;; 4 = count 4
;; 8 = count 7
;; 9 = count 6 & 4 
;; 0 = count 6 - 9 & 1
;; 6 = count 6 - 9 - 0 
;; 3 = count 5 & 1 
;; 5 = count 5 - 3 & 6 
;; 2 = count 5 - 3 - 5 

(defn p-and? [p1 p2]
  (let [c (min (count p1) (count p2))]
    (= c (count (intersection p1 p2)))))

(defn numbers [raw-patterns]
  (let [patterns (group-by count (map set raw-patterns))
        one   (get-in patterns [2 0])
        seven (get-in patterns [3 0])
        four  (get-in patterns [4 0])
        eight (get-in patterns [7 0])
        nine  (first (filter #(p-and? four %) (get patterns 6)))
        zero  (first (filter #(and (not= nine %) (p-and? one %)) (get patterns 6)))
        six   (first (filter #(not (#{zero nine} %)) (get patterns 6)))
        three (first (filter #(p-and? one %) (get patterns 5)))
        five  (first (filter #(and (not= three %) (p-and? nine %)) (get patterns 5)))
        two   (first (filter #(not (#{three five} %)) (get patterns 5)))]
    {zero  0
     one   1
     two   2
     three 3
     four  4
     five  5
     six   6
     seven 7
     eight 8
     nine  9}))

(defn part-2 [input]
  (->> input
       (map (fn [line]
              (let [[patterns digits] line
                    numbers (numbers patterns)]
                (->> digits
                     (map #(get numbers (set %)))
                     (apply str)
                     Integer/parseInt))))
       (apply +)))

(comment
  (part-1 test-input)
  (part-1 input)

  (part-2 test-input)
  (part-2 input))