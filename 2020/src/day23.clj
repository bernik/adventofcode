(ns day23
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]))

(def test-input ["sesenwnenenewseeswwswswwnenewsewsw"
                 "neeenesenwnwwswnenewnwwsewnenwseswesw"
                 "seswneswswsenwwnwse"
                 "nwnwneseeswswnenewneswwnewseswneseene"
                 "swweswneswnenwsewnwneneseenw"
                 "eesenwseswswnenwswnwnwsewwnwsene"
                 "sewnenenenesenwsewnenwwwse"
                 "wenwwweseeeweswwwnwwe"
                 "wsweesenenewnwwnwsenewsenwwsesesenwne"
                 "neeswseenwwswnwswswnw"
                 "nenwswwsewswnenenewsenwsenwnesesenew"
                 "enewnwewneswsewnwswenweswnenwsenwsw"
                 "sweneswneswneneenwnewenewwneswswnese"
                 "swwesenesewenwneswnwwneseswwne"
                 "enesenwswwswneneswsenwnewswseenwsese"
                 "wnwnesenesenenwwnenwsewesewsesesew"
                 "nenewswnwewswnenesenwnesewesw"
                 "eneswnwswnwsenenwnwnwwseeswneewsenese"
                 "neswnwewnwnwseenwseesewsenwsweewe"
                 "wseweeenwnesenwwwswnew"])

(def input (->> "day23.txt"
                io/resource
                io/reader
                line-seq))

(def shift {"e"  [ 2  0]
            "w"  [-2  0]
            "se" [ 1  1]
            "sw" [-1  1]
            "ne" [ 1 -1]
            "nw" [-1 -1]})


(defn black-tiles [input]
  (->> input
       (map (fn [line]
              (->> line
                   (re-seq #"e|se|sw|w|nw|ne")
                   (map #(get shift %))
                   (reduce (fn [[x y] [dx dy]] [(+ x dx) (+ y dy)])))))
       frequencies
       (filter (comp odd? second))
       (map first)))

(defn part-1 [input]
  (count (black-tiles input)))

(pprint (part-1 test-input))
(pprint (part-1 input))


(defn neighbours [db [x y]]
  (->> shift
       (map second)
       (map (fn [[dx dy]] 
              (get db [(+ x dx) (+ y dy)] :white)))))


(defn next-color [db tile]
  (let [color (get db tile :white)
        c (->> (neighbours db tile)
               (filter #(= :black %))
               count)]
    (cond
      (and (= color :black)
           (or (zero? c) (> c 2)))
      :white
      
      (and (= color :white) (= c 2))
      :black
      
      :else 
      color)))

(defn round [db]
  (let [[x1 x2] (apply (juxt min max) (map first (keys db)))
        [y1 y2] (apply (juxt min max) (map second (keys db)))]
    (->> (for [x (range (dec x1) (+ 2 x2))
               y (range (dec y1) (+ 2 y2))]
           [[x y] (next-color db [x y])])
         (into {}))))

(defn part-2 [input]
  (let [db (zipmap (black-tiles input) (repeat :black))]
    (->> db 
         (iterate round)
         (drop 100)
         first
         (filter #(= :black (second %)))
         count)))

(pprint (part-2 test-input))
(pprint (part-2 input))