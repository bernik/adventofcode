(ns adventofcode.day1
  (:require [clojure.java.io :as io]))

(def input (-> "day1.txt"
               io/resource
               io/file
               slurp))

(defn part-one []
  (->> input
       (map #(if (= \( %) 1 -1))
       (apply +)))

(defn part-two []
  (->> input
       (reductions 
        (fn [floor c] 
          (if (= c \() 
            (inc floor) 
            (dec floor)))
        0)
       (keep-indexed #(when (= %2 -1) %1))
       first))
