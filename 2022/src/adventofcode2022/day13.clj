(ns adventofcode2022.day13
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (filter #(not= "" %))
       (map read-string)))


(def test-input (parse-input "day13.test.txt"))
(def input (parse-input "day13.txt"))


(defn ->vec [x]
  (if (vector? x)
    x
    (vector x)))


(defn cmp [left right]
  (loop [[l & ls] (->vec left)
         [r & rs] (->vec right)]
    (cond 
      (and (nil? l) (nil? r)) 0

      (and (nil? l) r) -1
      (and l (nil? r)) 1

      (and (int? l) (int? r)) 
      (if (= l r)
        (recur ls rs)
        (compare l r))

      :else 
      (let [res (cmp l r)]
        (if (zero? res)
          (recur ls rs)
          res)))))


(defn part-1 [input]
  (->> input
    (partition 2)
    (map-indexed 
      (fn [i [l r]]
        (if (neg? (cmp l r))
          (inc i)
          0)))
    (apply +)))


(defn part-2 [input]
  (->> input 
    (into [[[2]] [[6]]])
    (sort cmp)
    (map-indexed 
      (fn [i x]
        (if (#{[[2]] [[6]]} x)
          (inc i)
          1)))
    (apply *)))



(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))





