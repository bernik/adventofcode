(ns adventofcode2021.day21)

;; Player 1 starting position: 7
;; Player 2 starting position: 10

(defn moves [player-two?]
  (->> (cycle (range 1 101))
       (drop (if player-two? 3 0))
       (partition 3 6)
       #_(map #(apply + %))))

(defn win [start player-two?]
  (->> (moves player-two?)
       (reductions (fn [[acc pos _ count] move] 
                     (let [pos' (inc (rem (dec (+ pos (apply + move))) 10))]
                       [(+ acc pos') pos' move (inc count)])) 
                   [0 start nil 0])
       (take-while #(< (first %) 1000))
       last))

(println (win 4 false))
(println (win 8 true))
(println :first (win 7 false))
(println :second (win 10 true))
      ;; 167

(println :x (+ 3 (* 143 3 2)))
(println (+ 3 (* 165 3 2)))

(->> (moves true)
      (reductions (fn [[acc pos] move] 
                    (let [pos' (inc (rem (dec (+ pos (apply + move))) 10))]
                      [(+ acc pos') pos' move])) 
                  [0 10 nil])
      (take 144)
      last)
