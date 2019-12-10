(ns adventofcode.day9
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def distances (->> "day9.txt"
                    io/resource
                    io/file 
                    slurp
                    str/split-lines
                    (reduce #(let [[_ from to dist] (re-find #"(\w+) to (\w+) = (\d+)" %2)]
                               (-> %1 
                                   (assoc-in [from to] (read-string dist))
                                   (assoc-in [to from] (read-string dist)))) 
                            {})))

(defn destination-by [criteria curr unvisited]
  (when-not (dead-end? curr unvisited)
    (->> (select-keys (get distances curr) unvisited)
         (apply criteria second))))

(defn dead-end? [curr unvisited]
  (or (not (contains? distances curr))
      (empty? (select-keys (get distances curr) unvisited))))

(defn distance-by [criteria from]
  (loop [distance 0
         curr from
         unvisited (disj (into #{} (keys distances)) from)]
    (if (empty? unvisited) 
      distance
      (if-let [[city dist] (destination-by criteria curr unvisited)]
        (recur (+ distance dist)
               city
               (disj unvisited city))
        :dead-end))))

(def shortest (partial distance-by min-key))
(def longest (partial distance-by max-key))

(defn part-one []
  (->> (keys distances)
       (map shortest)
       (filter #(not= :dead-end %))
       (apply min)))

(defn part-two []
  (->> (keys distances)
       (map longest)
       (filter #(not= :dead-end %))
       (apply max)))
