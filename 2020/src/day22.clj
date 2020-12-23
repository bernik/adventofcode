(ns day22
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [[9 2 6 3 1]
                 [5 8 4 7 10]])

(def input [[28 13 25 16 38 3 14 6 29 2 47 20 35 43 30 39 21 42 50 48 23 11 34 24 41]
            [27 37 9 10 17 31 19 33 40 12 32 1 18 36 49 46 26 4 45 8 15 5 44 22 7]])

;; part 1
(defn part-1 [input]
  (loop [[x & xs :as deck-1] (first input)
         [y & ys :as deck-2] (second input)]
    (if-not (and x y)
      (->> (or deck-1 deck-2)
           reverse
           (map-indexed #(* (inc %1) %2))
           (apply +))
      (if (< x y)
        (recur xs (into (vec ys) [y x]))
        (recur (into (vec xs) [x y]) ys)))))

(pprint (part-1 test-input))
(pprint (part-1 input))

;; part 2
(defn subgame [input game]
  (loop [[x & xs :as deck-1] (first input)
         [y & ys :as deck-2] (second input)
         history #{}
         round 1]
    (cond
      (not (and x y))
      [deck-1 deck-2]


      (contains? history [deck-1 deck-2])
      [deck-1 nil]

      (and (<= x (count xs))
           (<= y (count ys)))
      (let [[winner-1 _] (subgame [(take x xs) (take y ys)] (inc game))]
        (if winner-1
          (recur (into (vec xs) [x y])
                 ys
                 (conj history [deck-1 deck-2])
                 (inc round))
          (recur xs
                 (into (vec ys) [y x])
                 (conj history [deck-1 deck-2])
                 (inc round))))

      (< x y)
      (recur xs
             (into (vec ys) [y x])
             (conj history [deck-1 deck-2])
             (inc round))

      :else
      (recur (into (vec xs) [x y])
             ys
             (conj history [deck-1 deck-2])
             (inc round)))))


(defn part-2 [input]
  (let [[deck-1 deck-2] (subgame input 1)]
    (->> (or deck-1 deck-2)
         reverse
         (map-indexed #(* (inc %1) %2))
         (apply +))))

(pprint (part-2 test-input))
(pprint (part-2 input))
