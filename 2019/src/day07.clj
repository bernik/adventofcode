(ns day7 
  (:require [clojure.java.io :refer [file resource]]
            [clojure.string :refer [split]]
            [intcode-computer :as computer]))

(def input (into [] (map read-string (split (slurp (file (resource "day7.txt"))) #","))))


(defn permutations [colls]
  (if (= 1 (count colls))
    (list colls)
    (for [head colls
          tail (permutations (disj (set colls) head))]
      (cons head tail))))


(defn thruster-signal [program [phase1 phase2 phase3 phase4 phase5]]
  (->> 0
       (computer/run program 0 phase1)
       second
       (computer/run program 0 phase2)
       second
       (computer/run program 0 phase3)
       second
       (computer/run program 0 phase4)
       second
       (computer/run program 0 phase5)
       second))

(defn thruster-signal-loop [program phases]
  (loop [in 0
         [[status out memory cursor] & rest :as t] (mapv #(computer/run program 0 %) phases)]
    (if (every? #(= :exit (first %)) t)
      (->> t last second)
      (let [next (if (= :exit status) 
                   [status out]
                   (computer/run memory cursor in))]
        (recur (second next) (conj (vec rest) next))))))




(defn part1 [program]
  (->> (permutations (range 5))
       (map #(vector % (thruster-signal program %)))
       (sort-by second > )
       first))

(defn part2 [program]
  (->> (permutations (range 5 10))
       (map #(vector % (thruster-signal-loop program %)))
       (sort-by second > )
       first))

(comment
  (part1 input)
  (part2 input)

  (computer/run [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26
                 27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5]
                9
                0)

  (count (permutations (range 5)))

  (part1 [3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0])
  (part1 [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33
          1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0])


  (thruster-signal-loop [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26
                         27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5]
                        '(9 8 7 6 5))
  (part2 [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26
          27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5])

  (with-redefs [input [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33
                       1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0]]
    (->> 0
         (computer/run input 0 1)
         second
         (computer/run input 0 0)
        ;  (computer/run input 0 4)
        ;  (computer/run input 0 3)
        ;  (computer/run input 0 2)
         second
         #_()))
  ())