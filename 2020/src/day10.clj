(ns day10
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(def test-input-1 [16 10 15 5
                   1 11 7 19
                   6 12 4])

(def test-input-2 [28 33 18 42 31 14
                   46 20 48 47 24 23
                   49 45 19 38 39 11
                   1 32 25 35 8 17
                   7 9 4 2 34 10 3])

(def input (->> "day10.txt"
                io/resource 
                io/reader
                line-seq
                (map read-string)))

;; part 1
(defn part-1 [input]
  (->> input
       (sort >)
       (partition 2 1)
       (map #(apply - %))
       sort
       (partition-by identity)
       (map (comp inc count))
       (apply *)))

;; part 2
(defn part-2 [input]
  (let [end (+ 3 (apply max input))
        input' (sort (conj input end))]
    (get
     (reduce
      (fn [m x]
        (assoc m
               x
               (+ (get m (- x 1) 0)
                  (get m (- x 2) 0)
                  (get m (- x 3) 0))))
      {0 1}
      input')
     (+ 3 (apply max input)))))

(pprint (part-2 test-input-1))
(pprint (part-2 test-input-2))
(pprint (part-2 input))
