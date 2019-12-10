(ns adventofcode.day5
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines]]))

(def input (-> "day5.txt"
               io/resource 
               io/file 
               slurp 
               split-lines))

(defn nice? [s]
  (and 
   (> (count (re-seq #"[aeiou]" s)) 2)
   (re-find #"(.)\1" s)
   (not (re-find #"ab|cd|pq|xy" s))))

(defn better-nice? [s]
  (and 
   (re-find #"(..).*\1" s)
   (re-find #"(.).\1" s)))

(defn part-one []
  (->> input
       (filter nice?)
       count))

(defn part-two []
  (->> input
       (filter better-nice?)
       count))
