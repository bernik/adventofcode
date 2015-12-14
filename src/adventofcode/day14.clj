(ns adventofcode.day14
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [line]
    (let [[_ name speed fly rest] (re-find #"^(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds")]
        (list name (parse-srting speed) (parse-string fly) (parse-string rest))))

(def reindeers (->> "day14.txt"
                io/resource
                io/file
                slurp
                str/split-lines
                (reduce
                    (fn [res [name speed fly rest]
                         (assoc-in res name {:speed speed
                                             :fly fly
                                             :rest rest})])
                    {})))
