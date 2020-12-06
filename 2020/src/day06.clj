(ns day06
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def input (-> "day06.txt"
               io/resource
               io/file
               slurp
               (str/split #"\n\n")))

;; part 1 
(->> input 
     (map #(count (disj (set %) \newline)))
     (apply +)
     println)

;; part 2 
(->> input
     (map #(->> %
                str/split-lines
                (map set)
                (apply set/intersection)
                count))
     (apply +)
     println)
