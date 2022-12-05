(ns adventofcode2022.day05
  (:require [clojure.java.io :as io]))


;     [D]    
; [N] [C]    
; [Z] [M] [P]
;  1   2   3 
(def test-stacks [(list) (list :n :z) (list :d :c :m) (list :p)])

; [S]                 [T] [Q]        
; [L]             [B] [M] [P]     [T]
; [F]     [S]     [Z] [N] [S]     [R]
; [Z] [R] [N]     [R] [D] [F]     [V]
; [D] [Z] [H] [J] [W] [G] [W]     [G]
; [B] [M] [C] [F] [H] [Z] [N] [R] [L]
; [R] [B] [L] [C] [G] [J] [L] [Z] [C]
; [H] [T] [Z] [S] [P] [V] [G] [M] [M]
;  1   2   3   4   5   6   7   8   9 
(def stacks 
  [(list)
   (list :s :l :f :z :d :b :r :h)
   (list :r :z :m :b :t)
   (list :s :n :h :c :l :z)
   (list :j :f :c :s)
   (list :b :z :r :w :h :g :p)
   (list :t :m :n :d :g :z :j :v)
   (list :q :p :s :f :w :n :l :g)
   (list :r :z :m)
   (list :t :r :v :g :l :c :m)])


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (map #(->> % (re-seq #"\d+") (map read-string)))))


(def test-input (parse-input "day05.test.txt"))
(def input (parse-input "day05.txt"))


(defn solve [retain-order? stacks input]
  (loop [stacks stacks
         [[n from to :as x] & xs] input]
    (if (nil? x)
      (->> stacks (drop 1) (map (comp name first)) (apply str))
      (let [crates (take n (get stacks from))]
        (recur 
          (-> stacks
              (update from #(drop n %))
              (update to #(into % (if retain-order? (reverse crates) crates))))
          xs)))))


(def part-1 (partial solve false))
(def part-2 (partial solve true))


(comment 
  (part-1 test-stacks test-input)
  (part-1 stacks input)
  (part-2 test-stacks test-input)
  (part-2 stacks input))