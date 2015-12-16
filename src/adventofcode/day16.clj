(ns adventofcode.day16
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [line]
  (let [[_ sue-number params] (re-find #"Sue (\d+): (.+)" line)] 
    (->> params
         (re-seq #"(\w+): (\d+)")
         (reduce #(assoc %1 (keyword (get %2 1)) (read-string (get %2 2)))
                 {:number (read-string sue-number)}))))

(def input (->> "day16.txt"
                io/resource
                io/file
                slurp
                str/split-lines
                (map parse-line)))

(def facts {:children 3
            :cats 7
            :samoyeds 2
            :pomeranians 3
            :akitas 0
            :vizslas 0
            :goldfish 5
            :trees 3
            :cars 2
            :perfumes 1})

(defn range-equality [[k v]] 
  (let [cmp-fn (condp #(contains? %1 %2) k
                 #{:cats :trees} < 
                 #{:pomeranians :goldfish} > 
                 =)] 
    (cmp-fn (get facts k) v))) 

(defn equality [[k v]] 
  (= v (get facts k))) 

(defn similar-by [pred aunt] 
  (->> (dissoc aunt :number)
       (every? pred))) 

(def part-one (->> input 
                   (filter #(similar-by equality %))  
                   first
                   :number))

(def part-two (->> input 
                   (filter #(similar-by range-equality %))
                   first 
                   :number))
