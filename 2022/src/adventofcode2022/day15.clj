(ns adventofcode2022.day15
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (map (fn [line] 
              (let [[x1 y1 x2 y2] (->> (re-seq #"\d+" line)
                                       (map parse-long))]
                {:sensor [x1 y1]
                 :beacon [x2 y2]})))))


(def test-input (parse-input "day15.test.txt"))
(def input (parse-input "day15.txt"))


(defn distance [[x1 y1] [x2 y2]]
  (+ (abs (- x2 x1)) (abs (- y2 y1))))



(defn sensor-area [sensor beacon]
  (let [d (distance sensor beacon)]
    (->> (for [x (range (- (first sensor) d) (inc (+ (first sensor) d)))
               y (range (- (second sensor) d) (inc (+ (second sensor) d)))
               :when (>= d (distance [x y] sensor))]
           [x y]))))



(let [row 10
      [sx sy :as sensor] [8 7]
      [bx by :as beacon] [2 10]
      d (distance sensor beacon)
      dx (abs (- sy row))]
  (range (+ dx (- sx d)) (inc (- (+ sx d) dx))))


; (? - sx) + (row - sy) = d
; ? - sx + row - sy = d 
; ? = d - row + sx + sy
; ?1 = abs(?)
; ?2 = -(abs ?)


; (let [input test-input row 10]
(let [input input row 2000000]
  (->> input
    (map 
     (fn [{:keys [sensor beacon]}]
       (let [[sx sy] sensor
             [bx by] beacon
             d (distance sensor beacon)
             dx (abs (- sy row))]
         (if (<= (- sy d) row (+ sy d))
           (->> (range (+ dx (- sx d)) (inc (- (+ sx d) dx)))
             set
             )
           #{}))))
    (apply clojure.set/union)
    ; sort
    count))


; 5948482 too low

; (defn part-1 [input row]
;   (let [beacons (->> input
;                      (map :beacon)
;                      (into #{}))]
;     (->> input
;          (mapcat #(sensor-area (:sensor %) (:beacon %)))
;          (into #{})
;          (filter #(= row (second %)))
;          (filter #(not (beacons %)))
;          count)))

(comment 
  (part-1 test-input 10)
  (part-1 input 2000000)
  )


