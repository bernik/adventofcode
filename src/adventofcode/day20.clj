(ns adventofcode.day20)

(defn divisors [number]
  (mapcat (fn [divisor]
            (when (zero? (mod number divisor))
              (let [multiple (/ number divisor)]
                (if (= multiple divisor)
                  [divisor]
                  [divisor multiple])))) 
          (range 1 (inc (int (Math/sqrt (int number)))))))

(def part-one (->> (range)  
                   (map #(vector % (apply + (divisors %))))
                   (drop-while #(> 2900000 (second %)))
                   first
                   first))
