(ns day10
  (:require [clojure.java.io :refer [file resource]]
            [clojure.string :refer [split-lines]]))

(def input (->> (split-lines (slurp (file (resource "day10.txt"))))))