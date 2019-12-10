(ns day2 
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (into [] (map read-string (str/split (slurp (io/file (io/resource "day2.txt"))) #","))))

(defn get-val [state pos] (get state (get state pos)))

(defn run-program [state noun verb]
  (loop [state (-> state
                   (assoc 1 noun)
                   (assoc 2 verb))
         pos 0]
    (case (get state pos)
      99 (get state 0)
      1  (recur (assoc state (get state (+ pos 3))
                             (+ (get-val state (+ pos 1)) (get-val state (+ pos 2))))
                (+ pos 4))
      2  (recur (assoc state (get state (+ pos 3))
                             (* (get-val state (+ pos 1)) (get-val state (+ pos 2))))
                (+ pos 4)))))

;; part 1 
(println (run-program input 12 2))

;; part 2 
(doall
  (for [noun (range 100)
        verb (range 100)
        :let [res (run-program input noun verb)]
        :when (= res 19690720)]
    (println (+ verb (* 100 noun)))))
