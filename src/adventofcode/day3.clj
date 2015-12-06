(ns adventofcode.day3
  (:require [clojure.java.io :as io]))

(def input (slurp (io/file (io/resource "day3.txt"))))
(def santa-path (keep-indexed #(when (even? %1) %2) input))
(def robot-path (keep-indexed #(when (odd? %1) %2) input))

(defn get-visited [path]
  (->> path 
       (reduce (fn [{visited :visited [x y] :current :as res} move]
                 (let [next (case move
                              \> [(inc x) y]
                              \< [(dec x) y]
                              \^ [x (inc y)]
                              \v [x (dec y)])]
                   (-> res
                       (update :visited conj next)
                       (assoc :current next))))
               {:visited #{[0 0]}
                :current [0 0]})
       :visited))  

(defn part-one [] 
  (->> input 
       get-visited
       count))

(defn part-two []  
  (let [santa-visited (get-visited santa-path)
        robot-visited (get-visited robot-path)]
    (->> [santa-visited robot-visited]
         (reduce into #{})
         count)))
