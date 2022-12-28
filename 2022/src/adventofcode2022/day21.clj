(ns adventofcode2022.day21
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
    io/reader
    line-seq
    (map #(rest (re-matches #"(\w+): (?:(\d+)|(\w+) ([\+\-\*\/]) (\w+))" %)))
    (map (fn [[name1 num name2 op name3]]
           (if num
              [(keyword name1) (parse-long num)]
              [(keyword name1) {:op   (-> op symbol resolve)
                      :args [(keyword name2) (keyword name3)]}])))
    (into {})))


(def test-input (parse-input "day21.test.txt"))
(def input (parse-input "day21.txt"))


(defn shout [monkeys name]
  (let [m (monkeys name)]
    (if (int? m)
      m
      (apply (:op m) (map #(shout monkeys %) (:args m))))))


(defn part-1 [input]
  (with-redefs [shout (memoize shout)]
    (shout input :root)))



;; part 2 alternative implementation

(defn build-stack [input]
  (loop [q [:root]
         stack (list)]
    (if (empty? q)
      stack
      (let [k (peek q) 
            x (input k)]
        (cond 
          (nil? x) 
          (recur (pop q) stack)

          (int? x)
          (recur (pop q) (conj stack [k x]))

          :else
          (recur 
            (into (pop q) (:args x))
            (conj stack [k x])))))))


(defn eval-stack [stack]
  (loop [stack stack
         mem {}]
    (if (empty? stack)
      mem
      (let [[k x] (peek stack)]
        (recur 
          (pop stack)
          (assoc mem k (if (int? x)
                         x
                         (apply (:op x)
                                (map #(mem %) (:args x))))))))))


(defn part-2 [input] 
  (let [stack 
        (-> input
            (assoc-in [:root :op] #'clojure.core/compare)
            (dissoc :humn)
            build-stack)

        humn0 (-> stack (conj [:humn 0]) eval-stack :root)]
    (loop [[from to] [0 (quot Long/MAX_VALUE 1024)]]
      (let [mid (quot (+ to from) 2)

            res 
            (-> stack 
                (conj [:humn mid]) 
                eval-stack
                :root)]
        (case res 
           0 mid
           1 (recur (if (= 1 humn0)
                      [mid to]
                      [from mid]))
          -1 (recur (if (= 1 humn0) 
                      [from mid]
                      [mid to])))))))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input) )

