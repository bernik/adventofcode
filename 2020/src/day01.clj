(ns day01
  (:require [clojure.java.io :as io]))

(def input (->> "day01.txt"
                io/resource
                io/reader
                line-seq
                (map #(Integer/parseInt %))))

(defn solution-1 [input]
  (-> (for [x input
            y (rest input)
            :when (= 2020 (+ x y))]
        (* x y))
      first))

(defn solution-2 [input]
  (-> (for [x input
            y (rest input)
            z (rest (rest input))
            :when (= 2020 (+ x y z))]
        (* x y z))
      first))

; (println (solution-1 input))
; (println (solution-2 input))