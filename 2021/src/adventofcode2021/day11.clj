(ns adventofcode2021.day11)
  

(def test-input
  [[5 4 8 3 1 4 3 2 2 3]
   [2 7 4 5 8 5 4 7 1 1]
   [5 2 6 4 5 5 6 1 7 3]
   [6 1 4 1 3 3 6 1 4 6]
   [6 3 5 7 3 8 5 4 7 8]
   [4 1 6 7 5 2 4 6 4 5]
   [2 1 7 6 8 4 1 7 2 1]
   [6 8 8 2 8 8 1 1 3 4]
   [4 8 4 6 8 4 8 5 5 4]
   [5 2 8 3 7 5 1 5 2 6]])
   
(def input
  [[4 7 3 8 6 1 5 5 5 6]
   [6 7 4 4 4 2 3 7 4 1]
   [2 8 1 2 8 6 8 8 2 7]
   [8 8 4 4 3 6 5 6 2 4]
   [4 5 4 6 6 7 4 2 6 6]
   [4 5 1 8 6 7 4 2 7 8]
   [7 4 5 7 2 3 7 4 3 1]
   [4 5 2 4 8 7 3 2 4 7]
   [3 1 5 3 3 4 1 3 1 4]
   [3 7 2 1 4 1 4 6 6 7]])

(defn near [grid [col row]]
  (let [ns [[col (inc row)]
            [(inc col) row]
            [(inc col) (inc row)]
            [col (dec row) ]
            [(dec col) row]
            [(dec col) (dec row)]]]))