(ns day08
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def test-input 
  [[:nop 0]
   [:acc 1]
   [:jmp 4]
   [:acc 3]
   [:jmp -3]
   [:acc -99]
   [:acc 1]
   [:jmp -4]
   [:acc 6]])

(def input (->> "day08.txt"
                io/resource
                io/reader
                line-seq
                (mapv #(let [[op arg] (str/split % #" ")]
                         [(keyword op) (Integer/parseInt arg)]))))

(defn run [input] 
 (loop [offset 0
        acc 0
        stack #{}]
   (let [[op n] (get input offset)]
     (cond 
       (nil? op) 
       [:ok acc]

       (contains? stack offset) 
       [:inf acc]

       :else 
       (case op
         :nop (recur (inc offset) acc (conj stack offset))
         :acc (recur (inc offset) (+ acc n) (conj stack offset))
         :jmp (recur (+ offset n) acc (conj stack offset)))))))

;; part 1
(def part-1 (comp second run))

(println (part-1 test-input))
(println (part-1 input))

;; part 2
(defn part-2 [input]
  (let [indexes (->> input
                     (map-indexed #(vector %1 (first %2)))
                     (filter #(not= :acc (second %)))
                     (map first)) 
       patch (fn [[op n]] 
               (if (= :nop op)
                 [:jmp n]
                 [:nop n]))]
   (->> indexes
        (map #(run (update input % patch)))
        (filter #(= :ok (first %)))
        first
        second)))

(println (part-2 test-input))
(println (part-2 input))