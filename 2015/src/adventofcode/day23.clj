(ns adventofcode.day23
  (:require [clojure.java.io :refer [file resource]]
            [clojure.string :refer [split-lines split]]))

(def commands {"hlf" (fn [name] 
                       (fn [position registers]
                         [(inc position) (update registers name / 2)]))
               "tpl" (fn [name] 
                       (fn [position registers]
                         [(inc position) (update registers name * 3)]))
               "inc" (fn [name] 
                       (fn [position registers]
                         [(inc position) (update registers name inc)]))
               "jmp" (fn [offset]
                       (fn [position registers]
                         [(+ position (read-string offset)) registers]))
               "jie" (fn [s]
                       (let [[_ name offset-str] (re-find #"(\w+), ([+|-]?\w+)" s)] 
                         (fn [position registers]
                           (let [offset (if (even? (get registers name))
                                          (read-string offset-str)
                                          1)]
                             [(+ position offset) registers]))))
               "jio" (fn [s]
                       (let [[_ name offset-str] (re-find #"(\w+), ([+|-]?\w+)" s)] 
                         (fn [position registers]
                           (let [offset (if (= 1 (get registers name))
                                          (read-string offset-str)
                                          1)]
                             [(+ position offset) registers]))))})

(def program (->> (slurp (file (resource "day23.txt")))
                  split-lines 
                  (mapv #(split % #" " 2)))) 

(defn execute [{a "a" b "b"}] 
  (loop [position 0
         registers {"a" a 
                    "b" b}]
    (if-let [[command argument] (get program position)]
      (let [f ((get commands command) argument)
            [next-position next-registers] (f position registers)]
        (recur next-position next-registers))
      (get registers "b"))))

(def part-one (execute {"a" 0 "b" 0}))                
(def part-two (execute {"a" 1 "b" 0}))                
