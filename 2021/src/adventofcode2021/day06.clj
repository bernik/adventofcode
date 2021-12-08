(ns adventofcode2021.day06)

(def test-input [3 4 3 1 2])
(def input [1 4 3 3 1 3 1 1 1 2 1 1 1 4 4 1 5 5 3 1 3
            5 2 1 5 2 4 1 4 5 4 1 5 1 5 5 1 1 1 4 1 5
            1 1 1 1 1 4 1 2 5 1 4 1 2 1 1 5 1 1 1 1 4
            1 5 1 1 2 1 4 5 1 2 1 2 2 1 1 1 1 1 5 5 3
            1 1 1 1 1 4 2 4 1 2 1 4 2 3 1 4 5 3 3 2 1
            1 5 4 1 1 1 2 1 1 5 4 5 1 3 1 1 1 1 1 1 2
            1 3 1 2 1 1 1 1 1 1 1 2 1 1 1 1 2 1 1 1 1
            1 1 4 5 1 3 1 4 4 2 3 4 1 1 1 5 1 1 1 4 1
            5 4 3 1 5 1 1 1 1 1 5 4 1 1 1 4 3 1 3 3 1
            3 2 1 1 3 1 1 4 5 1 1 1 1 1 3 1 4 1 3 1 5
            4 5 1 1 5 1 1 4 1 1 1 3 1 1 4 2 3 1 1 1 1
            2 4 1 1 1 1 1 2 3 1 5 5 1 4 1 1 1 1 3 3 1
            4 1 2 1 3 1 1 1 3 2 2 1 5 1 1 3 2 1 1 5 1
            1 1 1 1 1 1 1 1 1 2 5 1 1 1 1 3 1 1 1 1 1
            1 1 1 5 5 1])


(def lanternfish
  (memoize
   (fn [n day]
     (cond
       (zero? day)
       1

       (zero? n)
       (+ (lanternfish 6 (dec day)) (lanternfish 8 (dec day)))

       :else
       (lanternfish (dec n) (dec day))))))

(defn total-lanternfishes [day input]
  (apply + (map #(lanternfish % day) input)))


(def part-1 (partial total-lanternfishes 80))
(def part-2 (partial total-lanternfishes 256))


(comment
  (part-1 test-input)
  (part-1 input)

  (part-2 test-input)
  (part-2 input))

