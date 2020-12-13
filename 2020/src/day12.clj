(ns day12
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))


(defn north [n [x y]] [x (+ y n)])
(defn south [n [x y]] [x (- y n)])
(defn east  [n [x y]] [(+ x n) y])
(defn west  [n [x y]] [(- x n) y])
(defn rotate-ship [n angle] (rem (+ 360 (+ angle n)) 360))
(defn rotate-waypoint [angle [x y]]
  (case (rem (+ 360 angle) 360)
    90  [(- y) x]
    180 [(- x) (- y)]
    270 [y (- x)]))

(defn forward [n {angle :angle :as x}]
  (update x
          :coordinates
          (case angle
            0   #(east n %)
            90  #(north n %)
            180 #(west n %)
            270 #(south n %))))

(defn forward-to-waypoint [n {[x y] :waypoint :as ship}]
  (-> ship
      (update :coordinates (partial (if (pos? x) east west) (* n (Math/abs x))))
      (update :coordinates (partial (if (pos? y) north south) (* n (Math/abs y))))))


(defn parse-line-1 [line]
  (let [[_ c n] (re-find #"(\w)(\d+)" line)
        n' (Integer/parseInt n)]
    (case c
      "N" #(update % :coordinates (partial north n'))
      "S" #(update % :coordinates (partial south n'))
      "E" #(update % :coordinates (partial east n'))
      "W" #(update % :coordinates (partial west n'))
      "R" #(update % :angle (partial rotate-ship (- n')))
      "L" #(update % :angle (partial rotate-ship n'))
      "F" #(forward n' %))))


(defn parse-line-2 [line]
  (let [[_ c n] (re-find #"(\w)(\d+)" line)
        n' (Integer/parseInt n)]
    (case c
      "N" #(update % :waypoint (partial north n'))
      "S" #(update % :waypoint (partial south n'))
      "E" #(update % :waypoint (partial east n'))
      "W" #(update % :waypoint (partial west n'))
      "R" #(update % :waypoint (partial rotate-waypoint (- n')))
      "L" #(update % :waypoint (partial rotate-waypoint n'))
      "F" #(forward-to-waypoint n' %))))


(def test-input ["F10"
                 "N3"
                 "F7"
                 "R90"
                 "F11"])

(def input (->> "day12.txt"
                io/resource
                io/reader
                line-seq))


(defn part-1 [input]
  (let [input' (map parse-line-1 input)
        {[x y] :coordinates}
        (reduce (fn [ship f] (f ship))
                {:coordinates [0 0] :angle 0}
                input')]
    (+ (Math/abs x) (Math/abs y))))

(println (part-1 test-input))
(println (part-1 input))

(defn part-2 [input]
  (let [input' (map parse-line-2 input)
        {[x y] :coordinates} 
        (reduce (fn [ship f] (f ship))
                    {:coordinates [0 0] :waypoint [10 1]}
                    input')]

    (+ (Math/abs x) (Math/abs y))))

(pprint (part-2 test-input))
(pprint (part-2 input))

