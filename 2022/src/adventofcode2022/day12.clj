(ns adventofcode2022.day12
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (mapv vec)))


(def test-input (parse-input "day12.test.txt"))
(def input (parse-input "day12.txt"))


(defn ->int [c]
  (case c
    \S (int \a)
    \E (int \z)
    (int c)))


(defn neighbours [m [row col]]
  (let [v (->int (get-in m [row col]))]
    (->> [[-1 0] [1 0] [0 -1] [0 1]]
      (map #(vector (+ row (first %)) (+ col (second %))))
      (filter #(and (get-in m %) 
                    (<= v (inc (->int (get-in m %)))))))))


(defn find-nodes [m v]
  (for [row (range (count m))
        col (range (count (first m)))
        :let [v' (get-in m [row col])]
        :when (= v v')]
    [row col]))


(defn bfs [input]
  (let [end-node (first (find-nodes input \E))]
    (loop [distances  {end-node 0}
           queue      [end-node]
           visited    #{}]
      (cond 
        (empty? queue)
        distances

        (visited (first queue))
        (recur distances (vec (rest queue)) visited)

        :else 
        (let [curr        (first queue)
              distance'   (inc (distances curr))
              neighbours' (neighbours input curr)
              distances'  (->> neighbours'
                               (reduce 
                                 (fn [ds n]
                                   (update 
                                     ds 
                                     n 
                                     (fnil #(min % distance') Long/MAX_VALUE)))
                                 distances))
              queue'      (->> (concat (rest queue) neighbours')
                               (into []))]
          (recur distances' queue' (conj visited curr)))))))


(defn part-1 [input]
  (get (bfs input) (first (find-nodes input \S))))


(defn part-2 [input]
  (let [a-set (set (find-nodes input \a))]
    (->> (bfs input)
      (filter #(a-set (first %)))
      vals
      (apply min))))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))