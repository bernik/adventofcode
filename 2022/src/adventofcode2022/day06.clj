(ns adventofcode2022.day06
  (:require [clojure.java.io :as io]))


(def samples 
  ;; input                               p1 p2
  [["mjqjpqmgbljsphdztnvjfqwrcgsmlb"     7  19]
   ["bvwbjplbgvbhsrlpgdmjqwftvncz"       5  23]
   ["nppdvjthqldpwncqszvftbrmjlhg"       6  23]
   ["nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"  10 29]
   ["zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"   11 26]])


(def input (slurp (io/resource "day06.txt")))


(defn solve [size input]
  (->> input
    (partition size 1)
    (keep-indexed #(when (= size (count (set %2))) %1))
    first
    (+ size)))


(def part-1 (partial solve 4))
(def part-2 (partial solve 14))


(comment 
  (map (comp part-1 first) samples)
  (part-1 input)
  (mapv (comp part-2 first) samples)
  (part-2 input)
  )