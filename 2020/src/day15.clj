(ns day15
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [0 3 6])
(def input [0 13 1 16 6 17])

(defn memory-game [input n]
  (let [init-mem  (->> input
                       (map-indexed #(vector %2 (list %1)))
                       (into {}))]
    (->> (range (count input) n)
         (reduce
          (fn [[prev mem] i]
            (let [next' (if (= 1 (count (get mem prev)))
                          0
                          (apply - (take 2 (get mem prev))))]
              [next' (update mem next' #(take 2 (conj % i)))]))
          [(last input) init-mem])
         first)))

;; part 1
(pprint (memory-game [0 3 6] 2020))
(pprint (memory-game input 2020))
;; part 2
(pprint (memory-game [0 3 6] 30000000))
(pprint (memory-game input 30000000))
