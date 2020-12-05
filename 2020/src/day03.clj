(ns day03
  (:require [clojure.java.io :as io]))

(def input (->> "day03.txt"
                io/resource
                io/reader
                line-seq
                (mapv #(into [] (seq %)))))

(defn trees-count [input right down]
  (let [rows (count input)
        cols (count (first input))]
    (->>
     (for [i (range 0 (inc (quot rows down)))
           :let [row (* i down)
                 col (rem (* i right) cols)]
           :when (= \# (get-in input [row col]))]
       [row col])
     count)))


;; part 1
(println (trees-count input 3 1))

;; part 2
(println (* (trees-count input 1 1)
            (trees-count input 3 1)
            (trees-count input 5 1)
            (trees-count input 7 1)
            (trees-count input 1 2)))