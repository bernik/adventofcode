(ns adventofcode.day7
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (-> "day7.txt"
               io/resource
               io/file
               slurp
               str/split-lines
               doall))

(def part-two? true)

(def wires (atom {}))
(def  get' (memoize #((get @wires %))))
(defn set' [name val] (swap! wires assoc name val))

(defn str->signal [signal-str]
  (condp re-find signal-str
    #"(\w+) RSHIFT (\d+)" :>> (fn [[_ x n]] #(bit-shift-right (get' x) (read-string n)))
    #"(\w+) LSHIFT (\d+)" :>> (fn [[_ x n]] #(bit-shift-left (get' x) (read-string n)))
    #"(\d+) AND (\w+)"    :>> (fn [[_ n x]] #(bit-and (read-string n) (get' x)))
    #"(\w+) AND (\d+)"    :>> (fn [[_ x n]] #(bit-and (read-string n) (get' x)))
    #"(\w+) AND (\w+)"    :>> (fn [[_ x y]] #(bit-and (get' x) (get' y)))
    #"(\d+) OR (\w+)"     :>> (fn [[_ n x]] #(bit-or (read-string n) (get' x)))
    #"(\w+) OR (\d+)"     :>> (fn [[_ x n]] #(bit-or (read-string n) (get' x)))
    #"(\w+) OR (\w+)"     :>> (fn [[_ x y]] #(bit-or (get' x) (get' y)))
    #"NOT (\w+)"          :>> (fn [[_ x]]   #(bit-and 16rFFFF (bit-not (get' x))))
    #"(\d+)"              :>> (fn [[_ n]]   (constantly (read-string n)))
    #"(\w+)"              :>> (fn [[_ x]]   #(get' x))))

(defn parse-str [line]
  (let [[signal-str name] (str/split line #" -> ")
        signal (if (and (= name "b")
                        part-two?) 
                 (constantly 956)
                 (str->signal signal-str))]
    (set' name signal)))

(defn solution []
  (for [line input] (parse-str line))
  (get' "a"))
