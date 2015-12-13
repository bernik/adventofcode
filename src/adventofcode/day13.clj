(ns adventofcode.day13
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(defn parse-line [line] 
  (let [[_ p1 action amount p2] (re-find #"^(\w+).+(lose|gain) (\d+).* (\w+)\.$" line)]
    (list p1 p2 (* 
                 (read-string amount)
                 (if (= action "lose") -1 1)))))

(def input-relations (->> "day13.txt" 
                          io/resource 
                          io/file 
                          slurp
                          str/split-lines
                          (map parse-line)
                          (reduce (fn [m [p1 p2 amount]] 
                                    (assoc-in m [p1 p2] amount)) {})))

(defn people->combinations [people] 
  (->> (disj (into #{} people) "Alice")
       combo/permutations
       (map #(concat ["Alice"] % ["Alice"]))))

(defn pair->happiness [pair relations]
  (->> pair 
       (partition 2 1)
       (pmap (fn [[p1 p2]] (+
                            (get-in relations [p1 p2])
                            (get-in relations [p2 p1]))))
       (apply +)))

(defn add-myself [relations]
  (reduce #(-> %1
               (assoc-in ["me" %2] 0)
               (assoc-in [%2 "me"] 0))
          relations
          (keys relations)))

(defn max-happiness [relations]
  (->> (keys relations) 
       people->combinations
       (pmap #(pair->happiness % relations))
       (apply max)))

(def part-one (max-happiness input-relations))

(def part-two 
  (->> input-relations
       add-myself 
       max-happiness))
