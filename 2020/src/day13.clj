(ns day13
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [939 [7 13 "x" "x" 59 "x" 31 19]]) 
(def input [1008713 [13 "x" "x" 41 "x" "x" "x" "x" "x" "x" "x" "x" "x" 467 "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" 19 "x" "x" "x" "x" 17 "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" 29 "x" 353 "x" "x" "x" "x" "x" 37 "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" "x" 23]])


(defn part-1 [input]
  (let [[ts bus-ids] input
        [bus-id time] (->> bus-ids
                           (filter #(not= "x" %))
                           (map #(vector % (* % (quot (+ ts %) %))))
                           (sort-by second)
                           first)]
    (* bus-id (- time ts))))


(pprint (part-1 test-input))
(pprint (part-1 input))

(defn part-2 [input]
 (let [input' (->> input
                   second
                   (map-indexed vector)
                   (filter #(not= "x" (second %))))]
   (first 
    (reduce (fn [[t step] [i n]]
              (let [t' (->> (iterate #(+ step %) t)
                            (filter #(zero? (rem (+ % i) n)))
                            first)]
                [t' (* step n)]))
            input'))))

(pprint (part-2 test-input))
(pprint (part-2 input))
