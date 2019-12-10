(ns adventofcode.day15
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-ingredient [line]
  (let [[_ name cap dur fl tex cal] (re-find #"(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)" line)]
    {:name name
     :capacity (read-string cap)
     :durability (read-string dur)
     :flavor (read-string fl)
     :texture (read-string tex)
     :calories (read-string cal)}))

(def ingredients (->> "day15.txt"
                      io/resource
                      io/file
                      slurp
                      str/split-lines
                      (map parse-ingredient)))

(def proportions
  (for [p1 (range 0 101)
        p2 (range 0 101)
        p3 (range 0 101)
        p4 (range 0 101)
        :when (= 100 (+ p1 p2 p3 p4))]
    [p1 p2 p3 p4]))

(defn score [ingredient]
  (->> (select-keys ingredient [:capacity :durability :flavor :texture])
       vals
       (map (fn [n] (if (> n 0) n 0)))
       (apply *)))

(defn apply-proportions [ingredients proportions]
  (->> ingredients 
       (interleave proportions)
       (partition 2)
       (map (fn [[proportion ingredient]]
              (reduce #(update %1 %2 (partial * proportion))
                      ingredient
                      [:capacity :durability :flavor :texture :calories])))
       (map #(-> % (dissoc :name)))
       (reduce #(-> %1
                    (update :capacity (partial + (:capacity %2)))
                    (update :durability (partial + (:durability %2)))
                    (update :flavor (partial + (:flavor %2)))
                    (update :texture (partial + (:texture %2)))
                    (update :calories (partial + (:calories %2)))))))

(def part-one 
  (->> proportions
       (map #(apply-proportions ingredients %))
       (map score)
       (apply max)))

(def part-two
  (->> proportions
       (map #(apply-proportions ingredients %))
       (filter #(= (:calories %) 500))
       (map score)
       (apply max)))
