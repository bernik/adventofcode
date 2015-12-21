(ns adventofcode.day20)

(defn dividers [n]
  (filter #(zero? (mod n %)) (range 1 (inc n))))

(def part-one (->> (range)
                   (map #(vector % (* 10 (apply + (dividers %)))))
                   (filter #(= 29000000 (second %)))
                   first))
