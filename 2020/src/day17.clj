(ns day17
  (:refer-clojure :exclude [cycle])
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [".#."
                 "..#"
                 "###"])

(def input ["..#....#"
            "##.#..##"
            ".###...."
            "#....#.#"
            "#.######"
            "##.#...."
            "#......."
            ".#......"])

(defn to-db [input] 
  (->> input 
       (map-indexed (fn [y line]
                      (->> line
                           (map-indexed (fn [x active] [[x y 0 0] (= active \#)]))
                           (into []))))
       (mapcat identity)
       (into {})))

(defn neigbours [db [x y z w] part2?]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        z' [(dec z) z (inc z)]
        w' (if part2? [(dec w) w (inc w)] [w])
        :when (not= [x y z w] [x' y' z' w'])
        :let [active' (get db [x' y' z' w'])]]
    [[x' y' z' w'] active']))

(defn next-cube [db [point active] part2?]
  (let [c (->> (neigbours db point part2?)
               (filter second)
               count)
        active' (if active
                  (<= 2 c 3)
                  (= 3 c))]
    [point active']))


(defn cycle [db part2?]
  (let [[x1 x2] (apply (juxt min max) (map #(nth % 0) (keys db)))
        [y1 y2] (apply (juxt min max) (map #(nth % 1) (keys db)))
        [z1 z2] (apply (juxt min max) (map #(nth % 2) (keys db))) 
        [w1 w2] (apply (juxt min max) (map #(nth % 3) (keys db))) ]
    (->> (for [x (range (dec x1) (+ 2 x2))
               y (range (dec y1) (+ 2 y2))
               z (range (dec z1) (+ 2 z2))
               w (range (dec w1) (+ 2 w2)) ]
           (next-cube db [[x y z w] (get db [x y z w])] part2?))
         (into {}))))

(defn boot-cycle [input part2?]
  (->> input
       to-db
       (iterate #(cycle % part2?))
       (drop 6)
       first
       (filter second)
       count))

;; part 1
(pprint (boot-cycle test-input false))
(pprint (boot-cycle input false))

;; part 2
(pprint (boot-cycle test-input true))
(pprint (boot-cycle input true))
