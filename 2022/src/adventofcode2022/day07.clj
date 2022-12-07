(ns adventofcode2022.day07
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq))


(def test-input (parse-input "day07.test.txt"))
(def input (parse-input "day07.txt"))


(defn parse-tree [input]
  (loop [[line & lines] input
         tree {"/" {}}
         path ["/"]]
    (if (nil? line)
      tree
      (let [[tree path] 
            (condp re-find line
              #"\$ cd \/" 
              [tree ["/"]]

              #"\$ cd \.\."
              [tree (pop path)]

              #"\$ cd (.*)"
              :>> #(vector tree (conj path (second %)))


              #"(\d+) (.*)" 
              :>> (fn [[_ size filename]]
                    (vector (assoc-in tree 
                                      (conj path filename) 
                                      (read-string size))
                            path))

              [tree path])]
        (recur lines tree path)))))


(def size 
  (memoize 
    (fn [tree]
      (->> tree
        (map (fn [[k v]]
               (if (int? v) 
                v
                (size v))))
        (apply +)))))


(defn dir-sizes [input]
  (let [tree (parse-tree input)
        children (fn [x] 
                   (->> (get-in tree x)
                        (filter (comp map? second))
                        (map #(->> % first (conj x)))))]
    (loop [[x & xs] [["/"]]
           res []]
      (if x
        (let [x-size (->> x (get-in tree) size)
              xs' (children x)]
          (recur (concat xs xs') 
                 (conj res x-size)))
        res))))


(defn part-1 [input]
  (->> (dir-sizes input)
       (filter #(<= % 100000))
       (apply +)))


;; part 2
(def available-space 70000000)
(def required-unused  30000000)

(defn part-2 [input]
  (let [sizes (dir-sizes input)
        used (apply max sizes)]
    (->> sizes
      sort
      (filter #(<= required-unused (- available-space (- used %))))
      first)))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))