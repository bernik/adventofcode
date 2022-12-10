(ns adventofcode2021.day14
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]
            [clojure.pprint  :refer [pprint]]))

(defn parse-input [path]
  (let [[template-str rules-str] (str/split (slurp (io/resource path)) #"\n\n")
        template (map  str template-str)
        template-counts (frequencies template)
        pairs-count (->> template
                   (partition 2 1)
                   (map #(vector (apply str %) 1))
                   (into {}))]
    [template-counts
     pairs-count
     (->> rules-str
          (re-seq #"(\w)(\w) -> (\w)")
          (map (fn [[_ a b c]] [(str a b) (str a c) (str c b) c]))) ]))


(def test-input (parse-input "day1 4.test.txt"))
test-input
(def input (parse-input "day1 4.txt"))
(nth test-input 2)

(defn apply-rules [rules counts pairs]
  (reduce
   (fn [[counts res] [from to-1 to-2 c]]
     (if-let [x (get pairs from)]
       [(update counts c (fnil #(+ x %) 1))
        (-> res
            (update to-1 (fnil inc 0))
            (update to-2 (fnil inc 0))) ]
       [counts res]))
   [counts pairs]
   rules))

(let [[c p r] test-input]
  (pprint (nth (iterate #(apply (partial apply-rules r) %) [c p]) 1 0)))

(->> (mapcat identity (repeat 1 (nth test-input 2)))
     (reduce (fn [[counts pairs pairs2] [pair add c]]
               (println counts pairs pair add c)
               (if-let [x (get pairs pair)]
                 [(update counts c (fnil inc 0))
                  pairs
                  (reduce #(update %1 %2 (fnil inc 0))
                          ;; (update pairs2 pair dec)
                          pairs2
                          add)
                  #_(reduce conj (disj pairs pair) add)]
                 [counts pairs pairs2]))
             [(first test-input) (second test-input) {}])
      ;; (filter #(pos? (second %)))
     pprint
     #_(filter #(= 1 (count (first %)))))

(mapcat identity (repeat 2 [[:foo] [:bar]]))


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
       (#(nth % 1 0))
       frequencies
       vals
       (sort >)
       ((juxt first last))
       (apply -)))

(comment
  (part-1 test-input)
  (part-1 input))