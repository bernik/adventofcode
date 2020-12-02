(ns day02
  (:require [clojure.java.io :as io]))

(defn parse-line [l]
  (let [[_ n1 n2 c s]
        (re-find #"(\d+)-(\d+) (.): (.+)" l)]
    [(read-string n1) (read-string n2) (first (seq c)) s]))

(def input (->> "day02.txt"
                io/resource
                io/reader
                line-seq
                (map parse-line)))

(defn solution-1 [input]
  (count 
   (filter (fn [[min max char string]]
             (let [n (get (frequencies string) char -1)]
               (and (<= n max)
                    (>= n min))))
           input)))

(defn solution-2 [input]
  (count 
   (filter (fn [[p1 p2 char string]]
             (or (and (=    char (get string (dec p1)))
                      (not= char (get string (dec p2))))
                 (and (=    char (get string (dec p2)))
                      (not= char (get string (dec p1))))))
           input)))

; (println (solution-1 input))
; (println (solution-2 input))
