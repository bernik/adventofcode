(ns adventofcode2021.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :refer [intersection difference]]
            [clojure.pprint :refer [pprint]]))

(defn parse-input [path]
  (let [[nums-str & boards-str]
        (str/split (slurp (io/file (io/resource path))) #"\n\n")

        nums (map read-string (str/split nums-str #","))
        boards (map (fn [board-str]
                      (mapv #(mapv read-string (str/split (str/trim %) #"\s+"))
                            (str/split board-str #"\n")))
                    boards-str)]
    [nums boards]))

(def transpose #(apply map vector %))

(defn winners [path]
  (let [[nums boards] (parse-input path)
        db' (->> boards
                (mapcat
                 (fn [board]
                   (map #(vector (set %) (set (flatten board)))
                        (concat (transpose board) board))))
                (into []))]
    (loop [[n & ns] nums
           db db'
           visited #{}
           winners []]
      (let [visited' (conj visited n)
            winners' (filter #(= 5 (count (intersection visited' (first %)))) db)
            winner-boards (into #{} (map second winners'))
            winner (second (last winners'))
            score (when winner
                    (* n (apply + (difference winner visited'))))]
        (if ns
          (recur ns
                 (if winner
                   (remove #(winner-boards  (second %)) db)
                   db)
                 visited'
                 (if winner
                   (conj winners [winner score n (count winners)])
                   winners))
          winners)))))

(def part-1 (comp second first winners))
(def part-2 (comp second last winners))

 

(comment
  (part-1 "day04.test.txt")
  (part-1 "day04.txt")
  (part-2 "day04.test.txt")
  (part-2 "day04.txt"))
  



