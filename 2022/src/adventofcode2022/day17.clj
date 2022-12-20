(ns adventofcode2022.day17
  (:require [clojure.java.io :as io]))


(defn parse-input [filename]
  (->> (io/resource filename)
       slurp
       (map (hash-map \> [1 0] \< [-1 0]))
       (into [])))


(def test-input (parse-input "day17.test.txt"))
(def input (parse-input "day17.txt"))


(def rocks 
  [{:h 1
    :w 4
    :p [[0 0] [1 0] [2 0] [3 0]]}

   {:h 3
    :w 3
    :p [      [1 2] 
        [0 1] [1 1] [2 1]
              [1  0]]}

   {:w 3
    :h 3
    :p [            [2 2]
                    [2 1]
        [0 0] [1 0] [2 0]]} 

   {:w 1
    :h 4
    :p [[0 3]
        [0 2]
        [0 1]
        [0 0]]}

   {:w 2
    :h 2
    :p [[0 1] [1 1]
        [0 0] [1 0]]}])



(defn move [rock [dx dy]]
  (update rock :p (fn [ps] (mapv #(vector (+ dx (first %)) (+ dy (second %))) ps))))


(defn collision? [rock rested]
  (some #(or (< 6 (first %))
             (neg? (first %))
             (neg? (second %))
             (rested %))
        (:p rock)))


(defn render [rested rock]
  (let [h (if (seq rested) 
            (->> rested 
                 (map second) 
                 (apply max)
                 (+ 4 (get rock :h 0)))
            10)
        rock? (into #{} (get rock :p []))]
  (print (str (char 27) "[2J")) ; clear screen
  (print (str (char 27) "[;H")) ; move cursor to the top left corner of the screen
  (println (repeat 7 "-"))
  (doseq [y (range h -1 -1)]
    (prn (->> (range 7)
              (map #(cond 
                     (rested [% y]) "#"
                     (rock? [% y])  "@"
                     :else          "."))
              (apply str))))))


(defn simulate [input limit]
  (loop [q            (cycle rocks)
         curr         nil
         jets         (cycle input)
         rested       #{}
         rocks-count  0]
    (let [total-height 
          (if (seq rested) 
            (->> rested 
                 (map second) 
                 (apply max)
                 inc)
            0)]
      ; (render rested curr)
      (println (str rocks-count "/" limit))
      (cond 
        (= limit rocks-count)
        total-height

        (nil? curr)
        (let [rock (first q)
              dy (+ 3 total-height)]
          (recur (rest q) 
                 (move rock [2 dy])
                 jets
                 rested
                 rocks-count))

        :else
        (let [x-moved (-> curr (move (first jets)))
              curr' (if (collision? x-moved rested)
                      curr
                      x-moved)
              y-moved (-> curr' (move [0 -1]))]
          (if (collision? y-moved rested)
            (recur q 
                   nil 
                   (rest jets) 
                   (into rested (:p curr'))
                   (inc rocks-count))
            (recur q 
                   y-moved
                   (rest jets)
                   rested
                   rocks-count)))))))

(defn part-1 [input] (simulate input 2022))
(defn part-2 [input] (simulate input 1000000000000))


(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input)
  )

