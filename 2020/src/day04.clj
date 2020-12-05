(ns day04
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split]]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.spec.alpha :as spec]))

(def raw-input (slurp (io/file (io/resource "day04.txt"))))

(def input (->> (split raw-input #"\n\n")
                (map (fn [x] 
                       (->> (split x #"\s+")
                            (map #(split % #":"))
                            (into {})
                            keywordize-keys)))))
(spec/def ::byr #(<= 1920 (read-string %) 2002))
(spec/def ::iyr #(<= 2010 (read-string %) 2020))
(spec/def ::eyr #(<= 2020 (read-string %) 2030))
(spec/def ::hgt
  #(let [[_ n m] (re-find #"(\d+)(cm|in)" %)]
     (case m
       "cm" (<= 150 (read-string n) 193)
       "in" (<= 59 (read-string n) 76)
       false)))
(spec/def ::hcl #(re-matches #"\#[0-9a-f]{6}" %))
(spec/def ::ecl #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})
(spec/def ::pid #(re-matches #"\d{9}" %))

(spec/def ::passport (spec/keys :req-un [::byr ::iyr ::eyr ::hgt ::hcl ::ecl ::pid]
                                :opt-un [::cid]))


;; part 1 
(println (->> input
              (filter #(every? % [:byr :iyr :eyr :hgt :hcl :ecl :pid]))
              count))

;; part 2
(->> input
     (filter #(spec/valid? ::passport %))
     count
     println)