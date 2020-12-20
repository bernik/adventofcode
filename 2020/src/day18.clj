(ns day18
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (->> "day18.txt"
                io/resource
                io/reader
                line-seq))

(defn is-digit? [x] (re-matches #"\d" x))

(defn ->rpn [line part2?]
  (loop [[x & xs] (-> line
                      (str/replace " " "")
                      (str/split #""))
         out []
         [op :as stack] (list)]

    (cond
      (nil? x) (into out stack)

      (is-digit? x)
      (recur xs (conj out (read-string x)) stack)

      (= "(" x)
      (recur xs out (conj stack x))

      (and part2?
           (#{"+" "*"} x))
      (if (and (= "+" op)
               (= "*" x))
        (let [[left right] (split-with #(= "+" %) stack)]
          (recur xs (into out left) (conj right x)))

        (recur xs out (conj stack x)))


      (#{"+" "*"} x)
      (if (#{"+" "*"} op)
        (let [[left right] (split-with #{"+" "*"} stack)]
          (recur xs (into out left) (conj right x)))

        (recur xs out (conj stack x)))

      (= ")" x)
      (let [[left [_ & right]] (split-with #(not= "(" %) stack)]
        (recur xs (into out left) right)))))


(defn calc [rpn]
  (loop [[x & xs] rpn
         [a b & rest :as stack] (list)]
    (cond
      (nil? x)
      (first stack)

      (number? x)
      (recur xs (conj stack x))
      
      (= "+" x)
      (recur xs (conj rest (+ a b)))

      (= "*" x)
      (recur xs (conj rest (* a b))))))

;; part 1 
(->> input
     (map (comp calc #(->rpn % false)))
     (apply +)
     pprint)

;; part 2
(->> input
     (map (comp calc #(->rpn % true)))
     (apply +)
     pprint)