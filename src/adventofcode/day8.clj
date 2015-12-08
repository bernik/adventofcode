(ns adventofcode.day8
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (-> "day8.clj"
                io/resource
                io/file
                slurp
                str/split-lines))


(def replace-all-trash
  (comp
    #(str/replace % #"(\\.)" "#")
    #(str/replace % #"(\\x[0-9a-f]{1,2})" "#")
    #(str/replace % #"(\\\\)" "#")))

(def text-symbols-count 
  (->> input
    (map #(replace-all-trash %))
	(map #(- (count %) 2))
	(apply +)))

(def escaped-text-symbols-count 
  (->> input
    (map #(str/escape % {\" "\\\"" \\ "\\\\"}))
    (map #(+ 2 (count %)))
    (apply +)))

(def code-symbols-count 
  (->> input
  (map count)
  (apply +)))

(defn part-one []
  (- code-symbols-count text-symbols-count))
  
(defn part-two []
  (- escaped-text-symbols-count code-symbols-count))
