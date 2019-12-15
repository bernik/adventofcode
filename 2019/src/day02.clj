(ns day2 
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [intcode-computer :as computer]))

(def input (into [] (map read-string (str/split (slurp (io/file (io/resource "day2.txt"))) #","))))

;; part 1 
(println (computer/run (assoc input 1 12 2 2) 0))

;; part 2 
(comment
  (doall
   (for [noun (range 100)
         verb (range 100)
         :let [[_ res] (computer/run (assoc input 1 noun 2 verb) 0)]
         :when (= res 19690720)]
     (println (+ verb (* 100 noun))))))
