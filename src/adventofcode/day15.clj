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

(defn update-vals [m vals f]
  (reduce #(update-in %1 [%2] f) m vals))

(comment (update-vals {:foo 1 :bar 2} [:foo :bar] (partial * 10)))

(defn get-proportions []
  (for [sprinkles (range 0 101)
        butterscotch (range 0 101)
        chocolate (range 0 101)
        candy (range 0 101)
        :when (= 100 (+ sprinkles butterscotch chocolate candy))]
    [sprinkles butterscotch chocolate candy]))
