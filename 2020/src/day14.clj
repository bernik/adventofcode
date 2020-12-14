(ns day14
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.math.combinatorics :refer [subsets]]))


(def test-input-1 
  ["mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
   "mem[8] = 11"
   "mem[7] = 101"
   "mem[8] = 0"])

(def test-input-2
  ["mask = 000000000000000000000000000000X1001X"
   "mem[42] = 100"
   "mask = 00000000000000000000000000000000X0XX"
   "mem[26] = 1"])

(def input (->> "day14.txt"
                io/resource 
                io/reader
                line-seq))

;; part 1
(defn value-mask-fn [mask]
  (->> mask
       reverse
       (map-indexed vector)
       (filter #(not= \X (second %)))
       (map (fn [[i op]]
              (case op
                \1 #(bit-set % i)
                \0 #(bit-clear % i))))
       (apply comp)))


(defn part-1 [input]
  (->> input
       (reduce
        (fn [[memory val-fn] line]
          (let [[_ mask addr val] (re-find #"mask = (\w+)|mem\[(\d+)\] = (\d+)" line)]
            (if mask
              [memory (value-mask-fn mask)]
              [(assoc memory addr (val-fn (Integer/parseInt val))) val-fn])
            ))
        [{} identity])
       first
       (map second)
       (apply +)))

(pprint (part-1 test-input-1))
(pprint (part-1 input))

;; part 2
(defn addr-mask-fn [mask]
  (let [mask' (->> mask
                   reverse
                   (map-indexed vector)) ]
    (fn [n]
      (let [n' (->> mask'
                    (filter #(not= \0 (second %)))
                    (reduce (fn [n [i op]]
                              (case op
                                \X (bit-clear n i)
                                \1 (bit-set n i)))
                            n))]
        (->> mask'
             (filter #(= \X (second %)))
             (map first)
             subsets
             (map (fn [xs] 
                    (if (empty? xs)
                      n'
                      (reduce #(bit-set %1 %2) n' xs)))))))))


(defn part-2 [input]
  (->> input
       (reduce
        (fn [[memory mf] line]
          (let [[_ mask addr val] (re-find #"mask = (\w+)|mem\[(\d+)\] = (\d+)" line)]
            (if mask
              [memory (addr-mask-fn mask)]

              [(reduce #(assoc %1 %2 (Integer/parseInt val)) 
                       memory
                       (mf (Integer/parseInt addr)))
               mf])))
        [{} identity])
       first
       (map second)
       (apply +)))

(pprint (part-2 test-input-2))
(pprint (part-2 input))