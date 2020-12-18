(ns day17
  (:refer-clojure :exclude [cycle])
  (:require [clojure.pprint :refer [pprint]]))

(def test-input-raw [".#." 
                     "..#" 
                     "###"])
(def test-input 
  (->> test-input-raw 
       (map-indexed (fn [y line]
                      (->> line
                           (map-indexed (fn [x active] [[x y 0] (= active \#)]))
                           #_(mapcat (fn [[[x y] active]]
                                     [[[x y -1] active]
                                      [[x y  0] active]
                                      [[x y  1] active]]))
                           (into []))))
       (mapcat identity)
       (into {})))

; (pprint test-input)

(defn neigbours [db [x y z]]
  (for [x' (range (dec x) (+ 2 x))
        y' (range (dec y) (+ 2 y))
        z' (range (dec z) (+ 2 z))
        :when (not= [x y z] [x' y' z'])
        :let [active' (get db [x' y' z'])]]
    [[x' y' z'] active']))

(defn next-cube [db [point active]]
  (let [c (->> (neigbours db point)
               (filter second)
               count)
        active' (if active
                  (<= 2 c 3)
                  (= 3 c))]
    [point active']))


(defn cycle [db]
  (let [[x1 x2] (apply (juxt min max) (map #(nth % 0) (keys db)))
        [y1 y2] (apply (juxt min max) (map #(nth % 1) (keys db)))
        [z1 z2] (apply (juxt min max) (map #(nth % 2) (keys db))) ]
    (->> (for [x (range (dec x1) (+ 2 x2))
               y (range (dec y1) (+ 2 y2))
               z (range (dec z1) (+ 2 z2))]
           (next-cube db [[x y z] (get db [x y z])]))
         (into {}))))

(pprint (->> test-input
            (iterate cycle)
            (drop 6)
            first
            ;;  cycle
             (filter second)
             count
             ))
