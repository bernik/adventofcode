(ns adventofcode2021.day14
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [path]
  (let [[template rules] (str/split (slurp (io/resource path)) #"\n\n")]
    [(map (comp keyword str) (seq template))
     (->> rules
          (re-seq #"(\w)(\w) -> (\w)")
          (reduce (fn [m [_ a b c]]
                    (assoc-in m [(keyword a) (keyword b)] (keyword c))) {}))]))


(def test-input (parse-input "day14.test.txt"))
(def input (parse-input "day14.txt"))


(defn step [init-template rules]
  (loop [[a b :as template] init-template
         res []]
    (if (nil? b)
      (conj res a)
      (if-let [c (get-in rules [a b])]
        (recur (rest template) (conj res a c))
        (recur (rest template) (conj res a))))))

(defn part-1 [[template rules]]
  (->> template
       (iterate #(step % rules))
       (#(nth % 10))
       frequencies
       vals
       (sort >)
       ((juxt first last))
       (apply -)))

(comment
  (part-1 test-input)
  (part-1 input))