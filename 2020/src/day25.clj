(ns day25
  (:require [clojure.pprint :refer [pprint]]))

(def test-input [5764801 17807724])
(def input [2084668 3704642])


(defn transform 
  ([key] (transform key 7 1))
  ([key subject] (transform key subject 1))
  ([key subject loops]
   (nth (iterate #(rem (* % subject) 20201227) key)
        loops)))

(defn loop-size 
  ([pk] (loop-size pk 1 7))
  ([pk init subject]
   (loop [i 0
          n init]
     (if (= n pk)
       i
       (recur (inc i) (transform n subject))))))


(pprint (loop-size (first test-input)))
(pprint (loop-size (second test-input)))


(defn part-1 [[pk1 pk2]]
  (transform pk2 pk2 (dec (loop-size pk1))))

(pprint (part-1 test-input))
(pprint (part-1 input))
