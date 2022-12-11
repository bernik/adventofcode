(ns adventofcode2022.day09
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map #(let [[_ d n] (re-matches #"(\w) (\d+)" %)]
            (list (keyword d) (parse-long n))))
    (mapcat (fn [[direction steps]]
              (repeat steps
                (case direction
                  :U [ 0 -1]
                  :D [ 0  1]
                  :L [-1  0]
                  :R [ 1  0]))))))


(def test-input (parse-input "day09.test.txt"))
(def test-input-2 (parse-input "day09-2.test.txt"))
(def input (parse-input "day09.txt"))


(defn add-coords [[ax ay] [bx by]]
  [(+ ax bx) (+ ay by)])


(defn sub-coords [[ax ay] [bx by]]
  [(- ax bx) (- ay by)])


;; for debug
(defn print-path [mark path]
  (let [visited (set path)
        [y1 y2] (->> visited 
                  (map second) 
                  ((juxt #(apply min %) #(apply max %))))
        [x1 x2] (->> visited 
                  (map first) 
                  ((juxt #(apply min %) #(apply max %))))]
    (doseq [line (->> (range y1 (+ 2 y2))
                   (map (fn [y] 
                          (->> (range x1 (+ 2 x2))
                            (map (fn [x] (if (visited [x y]) mark ".")))
                            (apply str)))))]
      (println line))))


(def head-path (partial reductions add-coords [0 0]))


(defn tail-path [head-path]
  (->> head-path
    (reductions 
      (fn [tail head]
        (let [diff (sub-coords head tail)
              far? (some #{-2 2} diff)]
          (if far?
            (->> diff
              (replace {-2 -1, 2 1})
              (add-coords tail))
            tail)))
      [0 0])
    dedupe))


(defn part-1 [input]
  (->> input
    head-path
    tail-path
    (into #{})
    count))


(defn part-2 [input]
  (->> input 
    head-path
    (iterate tail-path)
    (drop 9)
    first
    (into #{})
    count)
  )

(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 test-input-2)
  (part-2 input))
