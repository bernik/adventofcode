(ns adventofcode2021.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))


(defn parse-input [path]
  (->> (io/reader (io/resource path)
        line-seq
        (map seq))))

(def test-input (parse-input "day10.test.txt"))
(def input (parse-input "day10.txt"))

(defn flip [x] 
  (case x
    \( \)
    \[ \]
    \{ \}
    \< \>
    \) \( 
    \] \[ 
    \} \{ 
    \> \< ))

(def open-brackets #{\( \[ \{ \<} )
(def close-brackets (set (map flip open-brackets)))

(defn corrupted-points [line]
  (let [cost {\) 3 \] 57 \} 1197 \> 25137}]
  (loop [[prev :as visited] (list)
         [x & xs] line]
    (cond
      (nil? x) 0

      (open-brackets x)
      (recur (conj visited x) xs)

      (= prev (flip x))
      (recur (rest visited) xs)

      :else (cost x)))))


(defn part-1 [lines]
  (apply + (map corrupted-points lines)))


(defn incomplete-points [line]
  (let [cost {\) 1 \] 2 \} 3 \> 4}]
    (loop [visited (list)
           [x & xs] line]
      (cond
        (nil? x)
        (reduce #(+ (cost %2) (* 5 %1)) 0 (map flip visited))

        (open-brackets x)
        (recur (conj visited x) xs)

        (close-brackets x)
        (recur (rest visited) xs)))))

(defn part-2 [lines]
  (let [lines' (filter #(zero? (corrupted-points %)) lines)
        c (count lines')]
    (->> lines'
         (map incomplete-points)
         sort
         (drop (quot c 2))
         first)))


(comment
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))