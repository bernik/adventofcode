(ns adventofcode.day2
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split split-lines]]))

;; lwh - length width height
(defn lwh->squares [[l w h]] 
  (list (* l w) (* w h) (* l h)))

(defn total-square [squares]
  (+ 
   (* 2 (apply + squares))
   (apply min squares)))

(defn total-ribbon [lwh]
  (let [sorted (sort lwh)]
    (+ 
     (* 2 (apply + (take 2 sorted)))
     (apply * sorted))))

(def parsed-in 
  (->> "day2.txt"
       io/resource 
       io/file    
       slurp
       split-lines
       (map #(split % #"x"))
       (map (fn [coll] (map read-string coll)))))

(defn part-one []
  (->> parsed-in
       (map lwh->squares)
       (map total-square)
       (apply +)))

(defn part-two []
  (->> parsed-in
       (map total-ribbon)
       (apply +)))
