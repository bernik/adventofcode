(ns day07
  (:require [clojure.java.io :as io]))

(def test-input 
  ["light red bags contain 1 bright white bag, 2 muted yellow bags."
   "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
   "bright white bags contain 1 shiny gold bag."
   "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
   "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
   "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
   "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."])

(def input (->> "day07.txt"
                io/resource
                io/reader
                line-seq
                (filter #(not (re-find #"no other" %)))))

;; part 1
(defn part-1 [input]
  (let [db (->> input
                (map #(re-seq #"\w+ \w+(?= bag)" %))
                (mapcat (fn [[v & ks]] (map #(vector % v) ks)))
                (reduce (fn [m [k v]]
                          (update m k #(conj (or % []) v)))
                        {}))]
    (loop [xs ["shiny gold"]
           bags #{}]
      (let [xs' (mapcat #(get db %) xs)]
        (if (empty? xs')
          (count bags)
          (recur xs' (into bags xs')))))))

(println (part-1 test-input))
(println (part-1 input))

;; part 2
(defn parse-line [line]
  (let [[[_ _ color] & vs] (re-seq #"(\d+)? ?(\w+ \w+) bag" line)]
    [color (map #(hash-map :count (Integer/parseInt (second %)) 
                           :color (nth % 2)) 
                vs)]))


(defn part-2 [input]
  (let [db (->> input
                (map parse-line)
                (into {}))]
    (loop [xs ["shiny gold"]
           sum 0]
      (let [xs' (->> xs
                     (mapcat #(get db %))
                     (mapcat #(repeat (:count %) (:color %))))]
        (if (empty? xs')
          sum
          (recur xs' (+ sum (count xs'))))))))

(println (part-2 test-input))
(println (part-2 input))