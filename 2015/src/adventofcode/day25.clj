(ns adventofcode.day25)

(defn triangle-from [start first-step]
  (iterate (fn [[n step]] [(+ n step) (inc step)]) [start first-step]))

(defn coordinates->number [row column]
  (let [triangle-start (/ (* column (inc column)) 2)] 
    (first (first (drop (dec row) (triangle-from triangle-start column))))))

(def index (coordinates->number 2978 3083))

(def part-one (first (drop (dec index) (iterate #(rem (* % 252533) 33554393) 20151125))))
