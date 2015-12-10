(ns adventofcode.day10)

(defn look-and-say [s]
  (->> s 
       (partition-by identity)
       (mapcat (juxt count first))
       (apply str)))

(defn length-for-iteration [start n]
  (->> start
       (iterate look-and-say)
       (drop n)
       first
       count))

(defn part-one [] (length-for-iteration "1113122113" 40))
(defn part-two [] (length-for-iteration "1113122113" 50))



