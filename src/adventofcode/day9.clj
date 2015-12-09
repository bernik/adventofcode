(ns adventofcode.day9
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def distances (->> "day9.txt"
                    io/resource
                    io/file 
                    slurp
                    str/split-lines
                    (reduce #(let [[_ from to dist] (re-find #"(\w+) to (\w+) = (\d+)" %2)]
                               (assoc-in %1 [from to] (read-string dist))) 
                            {})))

#_(def distances {:london {:dublin 464
                           :belfast 518}
                  :dublin {:belfast 141}})

(def all-cities 
  (set/union
   (->> distances keys (into #{}))
   (->> distances 
        (map #(-> % second keys))
        (reduce #(into %1 %2) #{}))))

(defn next-destination [curr unvisited]
  (when-not (dead-end? curr unvisited)
    (->> (select-keys (get distances curr) unvisited)
         (apply min-key second))))

(defn dead-end? [curr unvisited]
  (or (not (contains? distances curr))
      (empty? (select-keys (get distances curr) unvisited))))

(defn shortest-distance [from]
  (loop [distance 0
         curr from
         unvisited (disj all-cities from)
         path (conj [] from)]
    (if (empty? unvisited) 
      distance
      (if-let [[city dist] (next-destination curr unvisited)]
        (recur (+ distance dist)
               city
               (disj unvisited city)
               (conj path city))
        {:dead-end path}))))

(defn part-one []
  (->> (keys distances)
       (map shortest-distance)
       #_(filter #(not= :dead-end %))
       #_(apply min)))

(comment 
  (part-one)
  (clojure.pprint/pprint all-cities)
  (clojure.pprint/pprint distances))
