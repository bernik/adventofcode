(ns adventofcode2021.day16
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))


(def input (slurp (io/resource "day16.txt")))

(defn hex->bin [^Character c]
  (as-> c c
        (Integer/parseInt (str c) 16)
        (Integer/toBinaryString c)
        (format "%4s" c)
        (str/replace c " " "0")))

(defn convert-string [s] (mapcat hex->bin s))


(defn header [packet]
  (let [[version-type rest] (split-at 6 packet)
        [version type] (split-at 3 version-type)]
    [version type rest]))


(defn bits->int [bits] (Integer/parseInt (apply str bits) 2))


(defn literal-length [bits]
  (->> bits
       (partition 5)
       (take-while #(= \1 (first %)))
       count
       inc
       (* 5)))


(defn literal-value [bits]
  (let [[val rest] (split-at (literal-length bits) bits)]
    [(apply str (mapcat next (partition 5 val))) rest]))


(defn operator [[mode & bits]]
  (if (= \0 mode)
    (let [[length tmp] (split-at 15 bits)
          [v rest] (split-at (bits->int length) tmp)]
      (println :0 (packets v) rest)
      [(packets v) rest])
    (let [[c tmp] (split-at 11 bits)
          [v bits] (packets tmp (bits->int c))]
      (println :1 [v] bits)
      [[v] bits])))


(defn packets 
 ([bits] (packets bits -1))
 ([bits n]
 (loop [res [] 
        bits bits]
   (println :b bits)
   (let [[version type tail] (header bits)]
     (println :v version :t type)
     (cond 
       (or (empty? bits) (every? #{\0} bits)) 
       res

       (= n (count res))
       [res bits]

       (= 4 (bits->int type))
       (let [[value tail'] (literal-value tail)
             packet {:type    :literal
                     :version (bits->int version) 
                     :value   value}]
         (recur (conj res packet) tail'))

       :else 
       (let [[value tail'] (operator tail)
             packet {:type    :operator
                     :version (bits->int version)
                     :args    value}]
         (recur (conj res packet) tail')))))))

(defn part-1 [input]
  (loop [acc 0
         xs (packets (convert-string input))]
    (if (empty? xs)
      acc
      (recur (+ acc (->> xs (map :version) (apply +)))
             (mapcat #(if-let [args (:args %)] args []) xs)))))



(comment
  (= 16 (part-1 "8A004A801A8002F478"))
  (packets (convert-string "8A004A801A8002F478"))
  (= 12 (part-1 "620080001611562C8802118E34"))
  (packets (convert-string "620080001611562C8802118E34"))
  (= 23 (part-1 "C0015000016115A2E0802F182340"))
  (= 31 (part-1 "A0016C880162017C3686B18A3D4780"))
  (packets (convert-string "38006F45291200"))
  (packets (convert-string "EE00D40C823060"))
  (pprint (packets (convert-string input)))
  (part-1 input)
  )