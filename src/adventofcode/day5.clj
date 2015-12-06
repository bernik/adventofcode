(ns adventofcode.day5)

(defn nice? [s]
  (and 
   (> (count (re-seq #"[aeiou]" s)) 2)
   (re-find #"(\w)\1" s)
   (not (re-find #"ab|cd|pq|xy" s))))
