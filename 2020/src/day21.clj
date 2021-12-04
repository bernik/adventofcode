(ns day21
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.set :refer [intersection]]))

(def input (->> "day21.txt"
                io/resource
                io/reader
                line-seq))

(def test-input ["mxmxvkd kfcds sqjhc nhms (contains dairy, fish)"
                 "trh fvjkl sbzzf mxmxvkd (contains dairy)"
                 "sqjhc fvjkl (contains soy)"
                 "sqjhc mxmxvkd sbzzf (contains fish)"])

(->> test-input
     (mapcat (fn [line]
               (let [[[& words] [_ & allergens]] 
                     (->> line
                          (re-seq #"\w+")
                          (split-with #(not= "contains" %)))]
                 (for [w words
                       a allergens]
                   [a w]))))
     (reduce (fn [m [a w]]
               (update-in m [w a] (fnil inc 0)))
             {})
     pprint)
