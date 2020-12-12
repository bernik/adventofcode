(ns day11
  (:require [clojure.java.io :as io]))

(def test-input 
  (->> (vector "L.LL.LL.LL"
               "LLLLLLL.LL"
               "L.L.L..L.."
               "LLLL.LL.LL"
               "L.LL.LL.LL"
               "L.LLLLL.LL"
               "..L.L....."
               "LLLLLLLLLL"
               "L.LLLLLL.L"
               "L.LLLLL.LL")
       (mapv #(->> % seq (into [])))))

(def input (->> "day11.txt"
                io/resource
                io/reader
                line-seq
                (mapv #(->> % seq (into [])))))

(defn up    [[row col]] [(dec row) col])
(defn down  [[row col]] [(inc row) col])
(defn left  [[row col]] [row (dec col)])
(defn right [[row col]] [row (inc col)])
(def up-right   (comp up right))
(def up-left    (comp up left))
(def down-right (comp down right) )
(def down-left  (comp down left))

(def moves [up down left right up-left up-right down-left down-right])

;; part 1 
(defn neighbours [input point] 
  (map (fn [move] (get-in input (move point) \.)) 
       moves))
;; part 2
(defn all-visible [input xy]
  (map (fn [move]
         (->> (iterate move (move xy))
              (map #(get-in input %))
              (drop-while #{\.})
              first))
       moves))

(defn next-seat [seat visible-seats occupied-number]
  (cond 
    (and (= seat \L)
         (not (some #{\#} visible-seats)))
    \#
    
    (and (= seat \#)
         (->> visible-seats
              (filter #{\#})
              count
              (<= occupied-number)))
    \L 

    :else seat))


(defn round [input visible-fn occupied-number]
  (let [rows-count (count input)
        cols-count (count (first input))]
    (->> (for [row (range 0 rows-count)
               col (range 0 cols-count)]
           [row 
            col
            (next-seat (get-in input [row col])
                       (visible-fn input [row col])
                       occupied-number)])
         (reduce (fn [res [row col val]]
                      (assoc-in res [row col] val))
                 input))))

(defn solve [input visible-fn occupied-number]
  (loop [cur input
         prev input]
    (let [next (round cur visible-fn occupied-number)]
      (if (not= prev next)
        (recur next cur)
        (->> cur
             flatten 
             (filter #{\#})
             count)))))

;; part 1
(println (solve test-input neighbours 4))
(println (solve input neighbours 4))

;; part 2
(println (solve test-input all-visible 5))
(println (solve input all-visible 5))