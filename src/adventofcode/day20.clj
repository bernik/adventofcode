(ns adventofcode.day20
  (:require [clojure.math.combinatorics :as combo]))

(defn get-dividers [n]
  (filter #(zero? (mod n %)) (range 1 (inc n))))

(comment (combo/subsets (reverse (dividers 10))))

(def dividers (get-dividers 2900000))

(def valid? (memoize (fn [subset] (= 2900000 (apply + subset)))))

(defn home-number [n]
  (let [part (take n dividers)]
    (println n)
    (some->> part
             combo/subsets
             (drop 1) ;; empty subset 
             (filter #(.contains % (last part)))
             (filter valid?)
             first
             (apply max))))

(comment (def part-one (->> (dividers 2900000)
                            reverse
                            combo/subsets
                            (drop 1)
                            (map (juxt #(apply max %) #(apply + %)))
                            (filter #(= 2900000 (second %)))
                            first)))

(comment (->> (range 1 (count dividers))
              (map home-number)
              (drop-while nil?)
              first))
