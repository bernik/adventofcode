(ns adventofcode2022.day25
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (into [])))


(def test-input (parse-input "day25.test.txt"))
(def input (parse-input "day25.txt"))


(defn snafu->long [snafu]
  (->> snafu
    reverse
    (map-indexed 
      (fn [exp n]
        (let [m {\2 2
                 \1 1
                 \0 0
                 \- -1
                 \= -2}
              d (m n)]
          (* d (long (Math/pow 5 exp))))))
    (apply +)))


(defn long->snafu [n]
  (loop [n n
         res (list)]
    (if (zero? n)
      (->> res
        (map #(get {-1 \- -2 \=} % %))
        (apply str))
      (let [q (quot n 5)
            r (rem n 5)]
        (if (< 2 r)
          (recur (inc q) (conj res (- r 5)))
          (recur q (conj res r)))))))


(defn part-1 [input]
  (->> input
    (map snafu->long)
    (apply +)
    long->snafu))


(comment 
  (part-1 test-input)
  (part-1 input)
  )