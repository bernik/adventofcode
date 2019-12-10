(ns adventofcode.day18
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

(def parse-line (comp 
                 (partial into {})
                 (partial map-indexed #(vector %1 (if (= \# %2) 1 0))))) 

(def grid (->> "day18.txt"
               io/resource 
               io/file 
               slurp
               str/split-lines
               (map-indexed #(vector %1 (parse-line %2)))
               (into {})))

(defn neighbors-on [grid [x y]]
  (reduce #(+ %1 (get-in grid %2 0))
          0
          (for [offset-x (range -1 2)
                offset-y (range -1 2)
                :when (not (and (zero? offset-x)
                                (zero? offset-y)))]

            [(+ x offset-x) (+ y offset-y)])))

(defn next-val [prev neighbors-on]
  (condp = [prev neighbors-on]
    [1 2] 1
    [1 3] 1
    [0 3] 1
    0))

(defn step [grid]
  (->> 
   (for [x (range (count (keys grid)))  
         y (range (count (keys (get grid 0))))]  
     [[x y] (next-val (get-in grid [x y]) (neighbors-on grid [x y]))])
   (reduce #(assoc-in %1 (first %2) (second %2)) grid)))

(defn turn-on-corners [grid]
  (reduce #(assoc-in %1 %2 1)
          grid
          (for [x [0 (dec (count (keys grid)))]
                y [0 (dec (count (keys (get grid 0))))]]
            [x y])))

(defn count-light [grid] 
  (->> grid 
       vals 
       (map #(apply + (vals %)))
       (apply +)))

(def part-one (-> (iterate step grid)   
                  (nth 100)
                  count-light))

(def part-two (-> (iterate (comp turn-on-corners step) (turn-on-corners grid))
                  (nth 100)
                  count-light))
