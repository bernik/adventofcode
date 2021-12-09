(ns adventofcode2021.day09
  (:require [clojure.java.io :as io]
            [clojure.set :refer [union difference]]))


(defn parse-input [path]
  (let [lines (line-seq (io/reader (io/file (io/resource path))))]
    (mapv (fn [l] 
            (mapv #(Character/digit % 10) (seq l))) 
          lines)))


(def test-input (parse-input "day09.test.txt"))
(def input (parse-input "day09.txt"))


(defn neighbours [[col row]]
  [[col (inc row)]
   [col (dec row)] 
   [(inc col) row] 
   [(dec col) row]])


(defn basin-locations [heightmap]
  (for [row (range (count (first heightmap)))
        col (range (count heightmap))
        :let [n (get-in heightmap [col row]) 
              neighbours (keep #(get-in heightmap %) (neighbours [col row]))]
        :when (not-any? #(<= % n) neighbours)]
      [col row]))


(defn part-1 [heightmap] 
  (apply + (map #(->> % (get-in heightmap) inc) 
                (basin-locations heightmap))))

;; part 2 

(defn valid? [heightmap [col row]]
  (when-let [n (get-in heightmap [col row])]
    (not= 9 n)))


(defn basin-size [heightmap root]
  (let [valid? (partial valid? heightmap)]
    (loop [visited #{root}
           xs (filter valid? (neighbours root))]
      (if (empty? xs)
        (count visited)
        (recur (union visited xs)
               (->> xs
                    (mapcat neighbours)
                    (into #{})
                    (#(difference % (set visited) (set xs)))
                    (filter valid?)))))))


(defn part-2 [heightmap]
  (->> (basin-locations heightmap)
       (map #(basin-size heightmap %))
       (sort >)
       (take 3)
       (apply *)))


(comment
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))
