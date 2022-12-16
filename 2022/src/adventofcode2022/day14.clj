(ns adventofcode2022.day14
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
        io/reader
        line-seq
        (mapcat #(->> (re-seq #"\d+" %)
                      (map parse-long)
                      (partition 2)
                      (partition 2 1)))))


(def test-input (parse-input "day14.test.txt"))
(def input (parse-input "day14.txt"))

(defn render [rocks sands]
  (do 
    (print (str (char 27) "[2J")) ; clear screen
    (print (str (char 27) "[;H")) ; move cursor to the top left corner of the screen
    (let [all (->> (into rocks sands))
          x-from (->> (map first all) (apply min))
          x-to (->> (map first all)
                    (apply max))
          y-to (->> (map second all)
                    (apply max))]
      (->> (range 0 (inc y-to))
           (map 
            (fn [y] 
              (->> (range x-from (inc x-to))
                   (map (fn [x] 
                          (cond
                            (rocks [x y]) "#"
                            (sands [x y]) "o"
                            :else ".")))
                   (apply str))))
           (clojure.string/join "\n") 
           print))))


(defn simulation [floor? input]
  (let [rocks (->> input
                   (mapcat 
                     (fn [[[ax ay] [bx by]]]
                       (for [x (range (min ax bx) (inc (max ax bx)))
                             y (range (min ay by) (inc (max ay by)))]
                         [x y])))
                   (into #{}))
        bottom (->> rocks (map second) (apply max) inc)]
    (loop [sands #{}
           [x y] [500 0]]
      (let [blocked? #(or (sands %)   
                          (rocks %)
                          (and floor? (= (second %) (inc bottom))))
            free? (comp not blocked?)
            down  [x (inc y)]
            left  [(dec x) (inc y)]
            right [(inc x) (inc y)]]
        (cond 
          ;; abyss 
          (and (not floor?) (= y bottom))
          (do 
            (render rocks sands)
            (count sands))

          ;; all rest
          (and floor? (sands [500 0]))
          (do 
            (render rocks sands)
            (count sands))

          ;; down
          (free? down)
          (recur sands down)

          ;; left
          (and (blocked? down) (free? left))
          (recur sands left)

          ;; right
          (and (blocked? down) (free? right))
          (recur sands right)

          :else 
          (recur (conj sands [x y]) [500 0]))))))


(def part-1 (partial simulation false))
(def part-2 (partial simulation true))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))


