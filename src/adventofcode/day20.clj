(ns adventofcode.day20)

(defn divisors [number]
  (mapcat (fn [divisor]
            (when (zero? (mod number divisor))
              (let [multiple (/ number divisor)]
                (if (= multiple divisor)
                  [divisor]
                  [divisor multiple])))) 
          (range 1 (inc (int (Math/sqrt (int number)))))))

(defn presents-one [number]
  (* 10 (apply + (divisors number))))

(defn presents-two [number] 
  (->> (divisors number)
       (filter #(<= (/ number %) 50))
       (map #(* 11 %))
       (apply +)))

(defn part-one [] 
  (->> (range)  
       (map #(vector % (presents-one %)))
       (drop-while #(> 29000000 (second %)))
       first
       first))

(defn part-two [] 
  (->> (range)  
       (map #(vector % (presents-two %)))
       (drop-while #(> 29000000 (second %)))
       first
       first))
