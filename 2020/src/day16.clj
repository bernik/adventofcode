(ns day16
  (:require [clojure.set :refer [intersection difference]]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]))

(def test-input-1
  {:rules ["class: 1-3 or 5-7"
           "row: 6-11 or 33-44"
           "seat: 13-40 or 45-50"]
   :your   [7 1 14]
   :nearby [[7 3 47]
            [40 4 50]
            [55 2 20]
            [38 6 12]]})

(def input (->> "day16.edn"
                io/resource
                io/file
                slurp
                read-string))


(defn parse-rules [rules]
  (->> rules
       (map
        (fn [line]
          (let [[_ r a1 b1 a2 b2] (re-find #"([\w ]+): (\d+)\-(\d+) or (\d+)\-(\d+)" line)]
            (vector (keyword (str/replace r #" " "-"))
                    (-> #{}
                        (into (range (Integer/parseInt a1) (inc (Integer/parseInt b1))))
                        (into (range (Integer/parseInt a2) (inc (Integer/parseInt b2)))))))))))

;; part 1 
(defn part-1 [{:keys [rules nearby]}]
  (let [valid (reduce #(into %1 (second %2)) #{} (parse-rules rules))]
    (->> nearby
         (mapcat identity)
         (filter #(not (valid %)))
         (apply +))))

(pprint (part-1 test-input-1))
(pprint (part-1 input))

;; part 2
(def test-input-2
  {:rules ["class: 0-1 or 4-19"
           "row: 0-5 or 8-19"
           "seat: 0-13 or 16-19"]
   :your   [11,12,13]
   :nearby [[3 9 18]
            [15 1 5]
            [5 14 9]]})


(defn resolve-fields [posible-fields]
  (loop [[[i pf] & xs] (->> posible-fields
                            (map-indexed vector)
                            (sort-by (comp count second) >))
         res []]
    (cond
      pf
      (recur xs (conj res [i (apply (partial difference pf) (map second xs))]))

      (every? #(= 1 (count (second %))) res)
      (->> res
           (map #(vector (first (second %)) (first %)))
           (into {}))

      :else
      (recur (sort-by (comp count second) > res) []))))

(defn part-2 [{:keys [rules your nearby]}]
  (let [rules' (parse-rules rules)
        valid  (reduce #(into %1 (second %2)) #{} rules')
        departure-fields [:departure-location
                          :departure-station
                          :departure-platform
                          :departure-track
                          :departure-date
                          :departure-time]
        field-index 
        (->> nearby
             (filter #(every? valid %))
             (map (fn [ticket]
                    (map (fn [n]
                           (->> rules'
                                (filter #((second %) n))
                                (map first)
                                (into #{})))
                         ticket)))
             (reduce (fn [res row] (map #(intersection %1 %2) res row)))
             resolve-fields)]
    (->> departure-fields
         (map #(get your (% field-index)))
         (apply *))))

(pprint (part-2 input))