(ns adventofcode2022.day11)


(def test-input 
  [{:items    [79 98]
    :op       #(*' % 19)
    :test-n   23
    :test     #(if (zero? (rem % 23)) 2 3)
    :inspects 0}

   {:items    [54 65 75 74]
    :op       #(+' % 6)
    :test-n   19
    :test     #(if (zero? (rem % 19)) 2 0)
    :inspects 0}

   {:items    [79 60 97]
    :op       #(*' % %)
    :test-n   13
    :test     #(if (zero? (rem % 13)) 1 3)
    :inspects 0}

   {:items    [74]
    :op       #(+' % 3)
    :test-n   17
    :test     #(if (zero? (rem % 17)) 0 1)
    :inspects 0}])

(def input 
  [{:items    [89, 74]
    :op       #(*' % 5)
    :test-n   17
    :test     #(if (zero? (rem % 17)) 4  7)
    :inspects 0}

   {:items    [75, 69, 87, 57, 84, 90, 66, 50]
    :op       #(+' % 3)
    :test-n   7
    :test     #(if (zero? (rem % 7)) 3  2)
    :inspects 0}

   {:items    [55]
    :op       #(+' % 7)
    :test-n   13
    :test     #(if (zero? (rem % 13)) 0  7)
    :inspects 0}

   {:items    [69, 82, 69, 56, 68]
    :op       #(+' % 5)
    :test-n   2
    :test     #(if (zero? (rem % 2)) 0  2)
    :inspects 0}

   {:items    [72, 97, 50]
    :op       #(+' % 2)
    :test-n   19
    :test     #(if (zero? (rem % 19)) 6  5)
    :inspects 0}

   {:items    [90, 84, 56, 92, 91, 91]
    :op       #(*' % 19)
    :test-n   3
    :test     #(if (zero? (rem % 3)) 6  1)
    :inspects 0}

   {:items    [63, 93, 55, 53]
    :op       #(*' % %)
    :test-n   5
    :test     #(if (zero? (rem % 5)) 3  1)
    :inspects 0}

   {:items    [50, 61, 52, 58, 86, 68, 97]
    :op       #(+' % 4)
    :test-n   11
    :test     #(if (zero? (rem % 11)) 5  4)
    :inspects 0}])


(defn monkeys-round [relief-worry? input]
  (reduce 
    (fn [monkeys i]
      (let [{:keys [items op test inspects]} 
            (get monkeys i)

            updates 
            (->> items
              (map 
                #(let [n (if relief-worry? 
                           (-> % op (quot 3)) 
                           (-> % op (rem (->> monkeys 
                                              (map :test-n) 
                                              (apply *)))))
                       i (test n)]
                   [i n])))

            monkeys' 
            (reduce 
              (fn [monkeys [i n]]
                (update-in monkeys [i :items] conj n))
              monkeys
              updates)]
        (-> monkeys'
          (assoc-in  [i :items] [])
          (update-in [i :inspects] + (count items)))))
    input
    (range (count input))))


(defn part-1 [input]
  (->> input
    (iterate (partial monkeys-round true))
    (take 21)
    last
    (map :inspects)
    (sort >)
    (take 2)
    (apply *)))


(defn part-2 [input]
  (->> input
    (iterate (partial monkeys-round false))
    (take (inc 10000))
    last
    (map :inspects )
    (sort >)
    (take 2)
    (apply *)))



(comment 
  (part-1 test-input)
  (part-1 input)
  (part-2 test-input)
  (part-2 input))

