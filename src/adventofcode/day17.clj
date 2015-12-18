(ns adventofcode.day17
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo :refer [subsets]]))

(def containers (->> "day17.txt"
                     io/resource
                     io/file
                     slurp
                     str/split-lines
                     (mapv read-string)))

(defn fit? [indexes] 
  (= 150
     (->> indexes
          (map #(get containers %))
          (apply +))))

(def fit-subsets (->> (subsets (range (count containers)))
                      (filter fit?)))

(def part-one (count fit-subsets))

(def part-two (let [min-subset-count (->> fit-subsets
                                          (map count)
                                          (apply min))]
                (->> fit-subsets
                     (filter #(= (count %) min-subset-count))
                     count)))
