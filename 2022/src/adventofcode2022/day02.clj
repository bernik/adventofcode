(ns adventofcode2022.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))


(defn parse-input [filename]
  (->> filename
    io/resource
    io/reader
    line-seq
    (map #(str/split % #" "))))


(def test-input (parse-input "day02.test.txt"))
(def input (parse-input "day02.txt"))

;; A - Rock 
;; B - Paper 
;; C - Scissors
;; A > C 
;; B > A 
;; C > B
(defn round-outcome [a b]
  (case [a b]
    ;; win
    (["A" "B"]
     ["B" "C"]
     ["C" "A"]) 6

    ;; draw
    (["A" "A"]
     ["B" "B"]
     ["C" "C"]) 3

    ;; loss
    0))


(def shape-points {"A" 1 "B" 2 "C" 3})


(defn round-score [[a b] strategy-fn]
  (let [b' (strategy-fn a b)]
    (+ (round-outcome a b')
       (get shape-points b'))))


(defn total-score [input strategy-fn]
  (apply + (map #(round-score % strategy-fn) input)))


(defn part-1 [input]
  (total-score input #(get {"X" "A", "Y" "B", "Z" "C"} %2)))


;; X - lose
;; Y - draw
;; Z - win
(defn part-2 [input]
  (total-score input 
    #(case [%1 %2]
       ;; A
       (["B" "X"]
        ["A" "Y"]
        ["C" "Z"]) "A"

       ;; B
       (["C" "X"]
        ["B" "Y"]
        ["A" "Z"]) "B"

       "C")))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input) )

