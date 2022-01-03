(ns adventofcode2021.day20
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))


(defn parse-input [path]
  (let [[algo img] 
        (str/split (slurp (io/resource path)) #"\n\n")
        
        img'      
        (->> img
             str/split-lines
             (mapv #(into [] %)))

        lights  
        (->> (for [col (range (count img'))
                   row (range (count (first img')))
                   :when (= \# (get-in img' [col row]))]
               [col row])
             (into #{}))
        ]
    {:algorythm (mapv #(if (= \. %) 0 1) algo)
     :image     img'
     :lights    lights}))

(def test-input (parse-input "day20.test.txt"))
(def input      (parse-input "day20.txt"))


(defn point->decimal [lights [col row]]
  (let [sector [[(dec col) (dec row)]
                [(dec col) row]
                [(dec col) (inc row)]
                [col (dec row)]
                [col row]
                [col (inc row)]
                [(inc col) (dec row)]
                [(inc col) row]
                [(inc col) (inc row)]]
        binary (->> sector
                    (map #(if (lights %) 1 0))
                    (apply str))]
    (Integer/parseInt binary 2)))


(defn enhance [algorythm lights]
  (let [[col-from col-to]
        (->> lights
             (map first)
             ((juxt #(apply min %) #(apply max %))))

        [row-from row-to]  
        (->> lights
             (map second)
             ((juxt #(apply min %) #(apply max %))))]

    (->> (for [row   (range (- row-from 1) (+ 2 row-to))
               col   (range (- col-from 1) (+ 2 col-to))
               :when (= 1 (get algorythm (point->decimal lights [col row])))]
           [col row])
         (into #{}))))


(defn print-img [lights]
  (let [[col-from col-to]
        (->> lights
             (map first)
             ((juxt #(apply min %) #(apply max %))))

        [row-from row-to] 
        (->> lights
             (map second)
             ((juxt (partial apply min) (partial apply max))))]

    (doseq [col (range (- col-from 2) (+ 3 col-to))]
      (println (->> (range (- row-from 2) (+ 3 row-to))
                    (map #(if (lights [col %]) "#" "."))
                    (apply str))))))

(defn part-1 [{:keys [algorythm lights]}]
  (let [f (partial enhance algorythm)]
    (count (f (f lights)))))


(let [{:keys [algorythm lights]} test-input
      f (partial enhance algorythm)]
  (println "--- start ---")
  (print-img lights)
  (println "----")
  (print-img (f lights))
  (println "----")
  (print-img (f (f lights)))
  )
(comment 
  (part-1 test-input)
  (part-1 input)
  )














