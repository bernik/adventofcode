(ns adventofcode2022.day15
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (map (fn [line] 
              (let [[x1 y1 x2 y2] (->> (re-seq #"-?\d+" line)
                                       (map parse-long))]
                {:sensor [x1 y1]
                 :beacon [x2 y2]})))))


(def test-input (parse-input "day15.test.txt"))
(def input (parse-input "day15.txt"))


(defn distance [[x1 y1] [x2 y2]]
  (+ (abs (- x2 x1)) (abs (- y2 y1))))


(defn merge-intervals [coll]
  (reduce 
    (fn [[[a b] :as res] [c d]]
      (cond 
        (nil? a)
        (conj res [c d])

        (<= c b d)
        (conj (rest res) [a d])

        (= 1 (- c b))
        (conj (rest res) [a d])

        (<= d b)
        res

        :else 
        (conj res [c d])))
    (list)
    coll))


(defn covered-intervals [input row]
  (->> input
    (map 
     (fn [{:keys [sensor beacon]}]
       (let [[sx sy] sensor
             [bx by] beacon
             d (distance sensor beacon)
             dx (abs (- sy row))]
         [(-> sx (- d) (+ dx)) (-> sx (+ d) (- dx))])))
    (filter #(< (first %) (second %)))
    sort
    merge-intervals))


(defn part-1 [input row]
  (let [beacons (->> input 
                     (map :beacon) 
                     (into #{})
                     (filter #(= row (second %)))
                     count)
        covered (->> (covered-intervals input row)
                     (map #(inc (- (second %) (first %))))
                     (apply +)) ]
    (- covered beacons)))


(defn part-2 [input limit]
  (->> (range limit)
       (map-indexed #(vector %1 (covered-intervals input %2)))
       (filter #(< 1 (count (second %))))
       (map (fn [[y xs]]
              (+ y
                 (->> xs 
                      (map second)
                      (apply min)
                      inc
                      (* 4000000)))))
       first))


(comment 
  (part-1 test-input 10)
  (part-1 input 2000000)
  (part-2 test-input 20)
  (part-2 input 4000000))


