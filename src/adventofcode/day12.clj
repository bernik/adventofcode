(ns adventofcode.day12
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def input (-> "day12.txt"
               io/resource
               io/file
               slurp
               json/read-str))

(defn get-sum [input branch-pred]
  (->> input
       (tree-seq branch-pred identity)
       (filter number?)
       (apply +)))

(def part-one (get-sum input coll?))
(def part-two (get-sum input #(or (vector? %)
                                  (and 
                                   (map? %)
                                   (not (.contains (vals %) "red"))))))
