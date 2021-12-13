(ns adventofcode2021.day13
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [path]
  (let [[points instructions] (str/split (slurp (io/file (io/resource path))) #"\n\n")]
    [(->> (str/split-lines points)
          (mapv #(read-string (str "[" % "]"))))
     (->> (str/split-lines instructions)
          (mapv #(let [[_ dir n] (re-matches #"fold along (\w)=(\d+)" %)]
                   [(keyword dir) (Integer/parseInt n)])))]))

(def test-input (parse-input "day13.test.txt"))
(def input (parse-input "day13.txt"))

(defn fold-y [points y]
  (->> points
       (map (fn [p]
              (if (< y (second p))
                (update p 1 #(- y (- % y)))
                p)))
       set))

(defn fold-x [points x]
  (->> points
       (map (fn [p]
              (if (< x (first p))
                (update p 0 #(- x (- % x)))
                p)))
       set))

(defn process-fold [points [dir n]]
  (case dir
    :x (fold-x points n)
    :y (fold-y points n)))

(defn print-points [points]
  (let [rows (inc (apply max (map second points)))
        cols (inc (apply max (map first points)))]
    (dotimes [n rows]
      (let [xs (set (map first (filter #(= n (second %)) points)))]
        (println (map #(if (xs %) "0" ".") (range cols)))))))

(defn part-1 [[points [fold]]]
  (count (process-fold points fold)))

(defn part-2 [[points instructions]]
  (->> instructions
       (reduce
        (fn [points [dir n]]
          (case dir
            :x (fold-x points n)
            :y (fold-y points n)))
        points)
       print-points))

(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))

