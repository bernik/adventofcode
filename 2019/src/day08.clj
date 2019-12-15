(ns day8
  (:require [clojure.java.io :refer [file resource]]))

(def input (seq (slurp (file (resource "day8.txt")))))

(def width 25)
(def height 6)

(defn part1 [input]
  (let [{n \1 m \2} (->> input
                         (partition (* width height))
                         (map frequencies)
                         (sort-by #(get % \0))
                         first)]
    (* n m)))

(defn part2 [input]
  (let [layers (partition (* width height) input)]
    (->> layers
         (apply interleave)
         (partition (count layers))
         (map (fn [pixels] (first (filter #(not= \2 %) pixels))))
         (map #(if (= \1 %) "*" " "))
         (partition width)
         (map #(apply str %)))))

  ;; => ("*    *  *  **  ***  *  * "
  ;;     "*    *  * *  * *  * *  * "
  ;;     "*    **** *    *  * **** "
  ;;     "*    *  * *    ***  *  * "
  ;;     "*    *  * *  * *    *  * "
  ;;     "**** *  *  **  *    *  * ")

