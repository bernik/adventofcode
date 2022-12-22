(ns adventofcode2022.day20
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (map-indexed #(vector %1 (parse-long %2)))))


(def test-input (parse-input "day20.test.txt"))
(def input (parse-input "day20.txt"))


(defn move [coll [idx n :as x]]
  (let [i (.indexOf (mapv first coll) idx)
        coll' (filter #(not= idx (first %)) coll)
        l (count coll')
        n' (mod n l)
        di (if (pos? n')
             (+ i n')
             (+ (* 2 (dec l)) n'))
        coll' (->> coll' 
                   cycle
                   (drop (+ i n'))
                   (take (dec (count coll))))]
    (into [x] coll')))


(defn grove-coordinates [coll]
  (let [l (count coll)
        zero (.indexOf coll 0)]
    (->> [(+ 1000 zero) (+ 2000 zero) (+ 3000 zero)]
         (map #(rem % l))
         (map #(nth coll %))
         (apply +))))


(defn part-1 [coll]
  (->> coll
       (reduce move coll)
       (mapv second)
       grove-coordinates))


(defn part-2 [coll]
  (let [coll' (->> coll (map #(vector (first %) (* 811589153 (second %)))))]
    (->> coll'
         cycle
         (take (* 10 (count coll')))
         (reduce move coll)
         (mapv second)
         grove-coordinates)))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))