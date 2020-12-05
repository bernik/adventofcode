(ns day05
  (:require [clojure.java.io :as io]))

(def input (->> "day05.txt"
                io/resource
                io/reader
                line-seq))

; оставить на память тупости
; вариант рабочий, но тупой
; (defn seat-id [input]
;   (loop [[c1 c2] [0 127]
;          [r1 r2] [0 7]
;          [x & xs] input]
;     (case x 
;       \F (recur [c1 (- c2 (inc (quot (- c2 c1) 2)))] [r1 r2] xs)
;       \B (recur [(+ c1 (inc (quot (- c2 c1) 2))) c2] [r1 r2] xs)
;       \L (recur [c1 c2] [r1 (- r2 (inc (quot (- r2 r1) 2)))] xs)
;       \R (recur [c1 c2] [(+ r1 (inc (quot (- r2 r1) 2))) r2] xs)
;       (+ r1 (* c1 8)))))

(defn seat-id [input]
  (let [[col row] 
        (->> input
             (replace {\F 0 \B 1 \R 1 \L 0})
             (split-at 7)
             (map #(read-string (str "2r" (apply str %)))))]
    (+ row (* col 8))))

;; part 1 
(println (->> input
              (map seat-id)
              (apply max)))

;; part 2
(println (->> input
              (map seat-id)
              sort
              (partition 2 1)
              (filter #(< 1 (- (second %) (first %))))
              ffirst
              inc))