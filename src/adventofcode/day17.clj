(ns adventofcode.day17
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defmacro combinations [sizes liters]
  (let [sizes# sizes
        symbols# (map #(symbol (str "n" %)) sizes#)
        ranges# (map #(list range 0 (inc (quot liters %))) sizes#)
        bindings# (-> (vec (interleave symbols# ranges#))
                      (conj :when (list '= liters 
                                        (list 'apply '+ (conj symbols# 'list)))))]
    `(for ~bindings# (vec ~@symbols#))))  

(combinations test-vec 10)

(defmacro combinations-2 [sizes liters]
  (let [sizes# sizes
        liters# liters]
    `(let [symbols# (map #(symbol (str "n" %)) ~sizes#)
           ranges# (map #(list range 0 (inc (quot ~liters# %))) ~sizes#)
           bindings# (-> (vec (interleave symbols# ranges#))
                         (conj :when (list '= ~liters# 
                                           (list 'apply '+ (conj symbols# 'list)))))]
       (foor bindings# (vec symbols#)))))

(defmacro combinations-3 [sizes liters]
  `(for 
    [~@(concat (interleave 
                (map #(symbol (str "n" %)) sizes) 
                (map #(list range 0 (inc (quot liters %))) sizes))
               (list :when (list '= liters 
                                 (list 'apply '+ (conj (map #(symbol (str "n" %)) sizes) 'list)))))] 
     [~@(map #(symbol (str "n" %)) sizes)]))

(defmacro for' [bindings body]
  (let [bindings# bindings
        body# body] 
    `(for [~@bindings] [~@body])))

(foor [n (range 2)] n)

(def c (combinations-2 test-vec 10))
(count (eval c))
(foor (first (combinations-2 test-vec 10)) (second (combinations-2 test-vec 10)))   
(macroexpand-1 '(combinations-2 test-vec 10))

(combinations-3 test-vec 10)
(clojure.pprint/pprint (macroexpand-1 '(combinations-3 test-vec 10)))

(def test-vec [1 2])
(combinations test-vec 10)

(defn gen-bind-body [sizes liters]
  (let [symbols (map #(gensym (str "n" %)) sizes)
        ranges (map #(list 'range 0 (inc (quot liters %))) sizes)
        bindings (concat (interleave symbols ranges)
                         (list :when (list '= liters 
                                           (list 'apply '+ (conj symbols 'list)))))]
    [bindings symbols])) 

(clojure.pprint/pprint (let [[binds body] (gen-bind-body (list 1 2) 10)]
                         (eval (list 'for' binds body))))
(eval (deref test-vec))

(def containers (->> "day17.txt"
                     io/resource
                     io/file
                     slurp
                     str/split-lines
                     (mapv read-string)))
(require '[clojure.pprint :refer [pprint]])
(def part-one (let [[binds body] (pprint (gen-bind-body containers 150))]
                (count (eval (list 'for' binds body)))))  
