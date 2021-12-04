(ns advetntofcode2021.day02
  (:require [clojure.java.io :as io]))

(defn parse-input [path]
  (->> (line-seq (->> path io/resource io/file io/reader))
       (map (fn [line]
              (let [[_ s n] (re-find #"(\w+) (\d+)" line)]
                [(keyword s) (read-string n)])))))

(defn part-1 [input]
  (->>
   (reduce (fn [[pos depth] [cmd n]]
             (case cmd
               :forward [(+ pos n) depth]
               :up      [pos (- depth n)]
               :down    [pos (+ depth n)]))
           [0 0]
           input)
   (apply *)))

(defn part-2 [input]
  (->>
   (reduce (fn [[pos depth aim] [cmd n]]
             (case cmd
               :forward [(+ pos n) (+ depth (* aim n)) aim]
               :up      [pos depth (- aim n)]
               :down    [pos depth (+ aim n)]))
           [0 0 0]
           input)
   drop-last
   (apply *)))

(comment
  (part-1 (parse-input "day02.txt"))
  (part-2 (parse-input "day02.txt"))
)
