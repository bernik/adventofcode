(ns adventofcode2022
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map #(->> % (re-seq #"\d+") (map parse-long)))
    (into #{})))


(def test-input (parse-input "day18.test.txt"))
(def input (parse-input "day18.txt"))


(defn neighbours [[x y z]]
  #{[(inc x) y z]
     [(dec x) y z]
     [x (inc y) z]
     [x (dec y) z]
     [x y (inc z)]
     [x y (dec z)]})


(defn area [cubes]
  (loop [[x & xs] cubes
         area 0]
    (if (nil? x)
      area
      (recur 
        xs
        (+ area
           (->> (neighbours x)
             (into #{})
             (clojure.set/intersection cubes)
             count
             (- 6)))))))


(def part-1 area)


(defn print-slices [cubes]
  (let [xs (->> cubes (map first) (apply max) inc)
        ys (->> cubes (map second) (apply max) inc)
        zs (->> cubes (map #(nth % 2)) (apply max) inc)] 
    (doseq [x (range xs)]
      (println (str "-- " x " --"))
      (let [cubes' (->> cubes 
                        (filter #(= x (first %))) 
                        (mapv rest)
                        (into #{}))
            grid (->> (repeat ys (->> (repeat zs ".") (into [])))
                      (into []))]
        (->> cubes'
             (reduce #(assoc-in %1 %2 "#") 
                     grid)
             (map #(apply str %))
             (clojure.string/join "\n")
             println)))))


(defn edges [cubes axis]
  (->> cubes
    (map #(nth % (.indexOf [:x :y :z] axis)))
    ((juxt #(apply min %) #(apply max %)))))


(defn outer-space [cubes]
  (let [expand #(vector (dec (first %)) (inc (second %)))
        [x1 x2] (expand (edges cubes :x))
        [y1 y2] (expand (edges cubes :y))
        [z1 z2] (expand (edges cubes :z))]
    (loop [[q & qq] [[x1 y1 z1]]
           visited #{}]
      (cond 
        (nil? q)
        visited

        (visited q)
        (recur qq visited)

        (cubes q) 
        (recur qq visited)

        :else 
        (let [nbrs 
              (->> 
                (neighbours q)
                (filter (fn [[x y z]]
                          (and (<= x1 x x2)
                               (<= y1 y y2)
                               (<= z1 z z2)))))]
          (recur 
              (into (vec qq) nbrs)
              (conj visited q)))))))


(defn part-2 [cubes]
  (let [outer (outer-space cubes)
        outer-area (area outer)
        distance #(->> (edges outer %)
                       (apply -)
                       abs
                       inc)
        w (distance :x)
        h (distance :y)
        d (distance :z)
        outer-outer-area (* 2 (+ (* w h) (* w d) (* h d)))]
    (- outer-area outer-outer-area)))


(comment
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input)
  (print-slices input))