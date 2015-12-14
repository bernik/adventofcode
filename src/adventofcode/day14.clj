(ns adventofcode.day14
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn line->list [line]
  (let [[_ name speed fly rest] (re-find #"^(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds" line)]
    (list name (read-string speed) (read-string fly) (read-string rest))))

(def input-reindeers (->> "day14.txt"
                          io/resource
                          io/file
                          slurp
                          str/split-lines
                          (map line->list)
                          (reduce
                           (fn [m [name speed fly rest]]
                             (assoc m name {:speed speed
                                            :fly fly
                                            :rest rest}))
                           {})))

(defn distance [reindeers name time]
  (let [{:keys [speed fly rest]} (get reindeers name)
        intervals-count (quot time (+ fly rest))
        time-remain (rem time (+ fly rest))]
    (+
     (* speed fly intervals-count)
     (* speed (min time-remain fly)))))

(defn winner [reindeers time]
  (->> (keys reindeers)
       (reduce #(assoc %1 %2 (distance reindeers %2 time))
               {})
       (sort-by second >)
       first
       first))

(def part-one (->> (keys input-reindeers) 
                   (map #(distance input-reindeers % 2503))
                   (apply max)))

(def part-two (->> (for [i (range 1 2504)] (winner input-reindeers i))
                   frequencies
                   (apply max-key val)
                   second))
