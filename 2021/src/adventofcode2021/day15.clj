(ns adventofcode2021.day15
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.pprint :refer [pprint]]))


(defn parse-input [path] (->> (read-string (slurp (io/resource path)))))

(def test-input (parse-input "day15.test.edn"))
(def input (parse-input "day15.edn"))


(defn neighbours [[row col]]
  [[(inc row) col]
   [(dec row) col] 
   [row (inc col)] 
   [row (dec col)]])


(defn risk [weights] 
  (let [width   (count (first weights))
        height  (count weights)]
    (loop [queue (priority-map [0 0] 0)
           costs (into [] (repeat height (into [] (repeat width ##Inf))))]
        (if (empty? queue)
          (get-in costs [(dec height) (dec width)] :not-found)
          (let [[pos cost] (first queue)]
            (if (= pos [(dec height) (dec width)])
              cost
              (let [updates 
                    (->> (neighbours pos)
                         (keep (fn [n]
                                 (when-let [w (get-in weights n)]
                                   (when (< (+ w cost) (get-in costs n))
                                     [n (+ w cost)])))))]
                (recur (reduce (fn [q [pos cost]] (assoc q pos cost))
                               (dissoc queue pos)
                               updates)
                       (reduce (fn [costs [pos cost]] (assoc-in costs pos cost))
                               costs
                               updates)))))))))

(def part-1 risk)


(defn enlarge-row [row delta-col]
  (let [width (count row)]
    (->> (cycle row)
         (take (* 5 width))
         (map-indexed #(inc (rem (+ -1 %2 delta-col (quot %1 width)) 9)))
         (into []))))


(defn enlarge [weights]
  (let [width  (count (first weights))
        height (count weights)]
    (->> (cycle weights)
         (take (* 5 height))
         (map-indexed #(enlarge-row %2 (quot %1 height)))
         (into []))))


(def part-2 (comp risk enlarge))

(comment 
  (part-1 test-input)
  (part-1 input) 
  (part-2 test-input)
  (part-2 input))
