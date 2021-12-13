(ns adventofcode2021.day12
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [path]
  (->> (line-seq (io/reader (io/resource path)))
       (map #(rest (re-matches #"(\w+)\-(\w+)" %)))
       (reduce (fn [m [a b]] (-> m
                                 (update a (fnil conj []) b)
                                 (update b (fnil conj []) a)))
               {})))

(def test-input (parse-input "day12.test.txt"))
(def test2-input (parse-input "day12.test2.txt"))
(def input (parse-input "day12.txt"))

(defn lower? [s] (= s (str/lower-case s)))
(defn upper? [s] (= s (str/upper-case s)))

(defn paths
  ([m] (paths false m "start" []))
  ([allow-twice? m] (paths allow-twice? m "start" []))
  ([allow-twice? m k res]
   (if (= k "end")
     [(conj res k)]
     (let [f (if allow-twice?
               #(let [fq (frequencies (filter lower? res))
                      n (fq % 0)]
                  (if (some #{2} (vals fq))
                    (zero? n)
                    (> 2 n)))
               #(not (.contains res %)))
           children
           (->> (get m k [])
                (filter #(not= "start" %))
                (filterv #(or (upper? %)
                              (= "end" %)
                              (f %))))]
       (if (empty? children)
         []
         (mapcat #(paths allow-twice? m % (conj res k)) children))))))

(def part-1 (comp count (partial paths)))
(def part-2 (comp count (partial paths true)))

(comment 
  (part-1 test-input)
  (part-1 test2-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 test2-input)
  (part-2 input)
  )