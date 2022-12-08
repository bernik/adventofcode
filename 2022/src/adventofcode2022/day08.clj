(ns adventofcode2022.day08
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (mapv #(->> % (re-seq #"\d") (mapv read-string)))))


(def test-input (parse-input "day08.test.txt"))
(def input (parse-input "day08.txt"))


(defn lines [m [col row]]
  (let [h (->> (range (count m))
               (map #(get-in m [col %]))
               ((juxt #(->> % (take row) reverse) 
                      #(drop (inc row) %))))
        v (->> (range (count m))
               (map #(get-in m [% row]))
               ((juxt #(->> % (take col) reverse) 
                      #(drop (inc col) %))))]
  (concat h v)))


(defn part-1 [input]
  (->> 
    (for [col (range 1 (dec (count input)))
          row (range 1 (dec (count (first input))))
          :let [lines (lines input [col row])
                tree (get-in input [col row])
                visible? (partial every? #(> tree %))]
          :when (some visible? lines)]
      [row col])
    count
    (+ (-> input count (* 4) (- 4)))))

;; part 2 
(defn viewing-distance [tree line]
  (let [[a b] (split-with #(> tree %) line)]
    (+ (count a) (if (seq b) 1 0))))


(defn scenic-score [tree lines]
  (->> lines 
       (map (partial viewing-distance tree))
       (apply *)))


(defn part-2 [input]
  (->> (for [col (range 1 (dec (count input)))
             row (range 1 (dec (count (first input))))
             :let [lines (lines input [col row])
                   tree (get-in input [col row])]]
         (scenic-score tree lines))
       (apply max)))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))


