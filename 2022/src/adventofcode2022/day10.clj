(ns adventofcode2022.day10
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map (fn [line] 
           (let [[op n] (clojure.string/split line #" ")]
            [(keyword op) (parse-long (or n "0"))])))))


(def test-input (parse-input "day10.test.txt"))
(def input (parse-input "day10.txt"))


(defn strength [cycle state]
  (if (or (= cycle 20) 
          (and (>= cycle 60)
               (-> cycle (- 20) (rem 40) zero?)))
    (* cycle state)
    0))


(defn process-ops [input]
  (loop [res   []
         state 1
         [[command n] & ops] input]
    (case command
      :noop (recur (-> res (conj state))
                   state
                   ops)
      :addx (recur (-> res (conj state) (conj state))
                   (+ state n)
                   ops)
      res)))


(defn part-1 [input]
  (->> input
     process-ops
     (map-indexed #(strength (inc %1) %2))
     (apply +)))


(defn part-2 [input]
  (->> input
    process-ops
    (map-indexed #(if (<= (dec %2) (rem %1 40) (inc %2))
                     "#"
                     "."))
    (partition 40)
    (map #(apply str %))
    (clojure.string/join "\n")))


(comment 
  (part-1 test-input)
  (part-1 input)
  (println (part-2 test-input))
  (println (part-2 input))
  ; ###...##..#....###..###..####..##..#..#.
  ; #..#.#..#.#....#..#.#..#....#.#..#.#..#.
  ; #..#.#....#....#..#.###....#..#..#.#..#.
  ; ###..#.##.#....###..#..#..#...####.#..#.
  ; #.#..#..#.#....#.#..#..#.#....#..#.#..#.
  ; #..#..###.####.#..#.###..####.#..#..##..
  )