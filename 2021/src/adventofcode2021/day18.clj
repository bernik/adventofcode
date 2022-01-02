(ns adventofcode2021.day18
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-pair [s]
  (->> s 
       (keep #(case %
                \[ "[" 
                \] "]"
                \, nil
                (Character/digit % 10)))
       (into [])))


(def test-input (->> "day18.test.txt"
                     io/resource
                     io/reader
                     line-seq
                     (mapv parse-pair)))


(def input (->> "day18.txt"
                io/resource
                io/reader
                line-seq
                (mapv parse-pair)))


(defn add-pairs [a b] (concat ["["] a b ["]"]))


(defn has-explodes? [coll]
  (loop [[x & xs] coll
         i 0
         c 0]
    (cond 
      (nil? x) nil

      (> c 4) (dec i)

      (= x "[") (recur xs (inc i) (inc c))

      (= x "]") (recur xs (inc i) (dec c))

      :else (recur xs (inc i) c))))


(defn has-splits? [coll] 
  (->> coll
       (keep-indexed #(when (and (number? %2) (> %2 9)) %1))
       first))


(defn last-number-idx [xs]
  (->> xs 
       (keep-indexed #(when (number? %2) %1))
       last))


(defn first-number-idx [xs]
  (->> xs 
       (keep-indexed #(when (number? %2) %1))
       first))


(defn explode-pair [coll i]
  (let [[l [_ n1 n2 _ & r]] (split-at i coll)
        l-idx (last-number-idx l)
        r-idx (first-number-idx r)]
    (concat 
      (if l-idx
        (update (into [] l) l-idx + n1)
        l)
      [0]
      (if r-idx
        (update (into [] r) r-idx + n2)
        r))))


(defn split-pair [coll i]
  (let [[l [n & r]] (split-at i coll)]
    (into []
      (concat l 
              ["[" (int (Math/floor (/ n 2))) (int (Math/ceil (/ n 2))) "]"]
              r))))


(defn reduce-pair [input]
  (loop [pair input]
    (if-let [explode-idx (has-explodes? pair)]
      (recur (explode-pair pair explode-idx))
      (if-let [split-idx (has-splits? pair)]
        (recur (split-pair pair split-idx))
        pair))))


(defn magnitude [pair]
  (letfn [(m [x]   
            (if (number? x) 
              x
              (let [[a b] x]
                (+ (* 3 (m a))
                   (* 2 (m b))))))]
    (m (read-string (str/replace (apply str pair) #"(\d)" " $1")))))

(defn part-1 [input]
 (magnitude (reduce #(reduce-pair (add-pairs %1 %2)) input)))


(defn part-2 [input]
 (->> (for [a input
            b input
            :when (not= a b)
            :let [m1 (magnitude (reduce-pair (add-pairs a b)))
                  m2 (magnitude (reduce-pair (add-pairs b a)))]]
        (max m1 m2))
      (apply max)))

(comment 
  (part-1 input) 
  (part-2 input))






