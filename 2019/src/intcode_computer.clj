(ns intcode-computer)

(def debug? false)

(defn idx [memory cursor base mode]
  (case mode
    "0" (get memory cursor)
    "1" cursor
    "2" (+ base (get memory cursor))))

(defn get-val [memory cursor base mode]
  (get memory (idx memory cursor base mode)))

(def instruction-names {"99" :halt
                        "01" :add 
                        "02" :multiply
                        "03" :read-input  
                        "04" :print  
                        "05" :jump-if-true
                        "06" :jump-if-false 
                        "07" :less-than 
                        "08" :equals
                        "09" :adjust-relative-base})

(defn parse-instruction [s]
  (let [[_ m3 m2 m1 op] (re-find #"(\d)(\d)(\d)(\d\d)" (format "%05d" (int s)))]
    {:op    (instruction-names op)
     :mode1 m1
     :mode2 m2
     :mode3 m3}))



(defn run [memory cursor & input]
  (loop [memory (if (map? memory) 
                  memory 
                  (into {} (map-indexed vector memory)))
         cursor cursor
         base 0
         input input
         output nil]
    (let [{:keys [op mode1 mode2 mode3]} (parse-instruction (get memory cursor))]
      (when debug?
        (println (list ;memory 
                  cursor
                  base
                  op
                  [(get-val memory (+ cursor 1) base "1")
                   (get-val memory (+ cursor 1) base mode1)
                   mode1]
                  [(get-val memory (+ cursor 2) base "1")
                   (get-val memory (+ cursor 2) base mode2)
                   mode2]
                  [(get-val memory (+ cursor 3) base "1")
                   (get-val memory (+ cursor 3) base mode3)
                   mode3])))
        
      (case op
        :halt
        [:exit (or output (get memory 0))]

        :add
        (recur (assoc memory
                      (idx memory (+ cursor 3) base mode3)
                      (+ (get-val memory (+ cursor 1) base mode1)
                         (get-val memory (+ cursor 2) base mode2)))
               (+ cursor 4)
               base
               input
               output)

        :multiply
        (recur (assoc memory
                      (idx memory (+ cursor 3) base mode3)
                      (* (get-val memory (+ cursor 1) base mode1)
                         (get-val memory (+ cursor 2) base mode2)))
               (+ cursor 4)
               base
               input
               output)

        :read-input
        (if (seq input)
          (recur (assoc memory
                        (idx memory (+ cursor 1) base mode1)
                        (bigint (first input)))
                 (+ cursor 2)
                 base
                 (rest input)
                 output)
          [:pending output memory cursor base])

        :print
        (recur memory
                (+ cursor 2)
                base
                input
                (get-val memory (+ cursor 1) base mode1))

        :jump-if-true
        (recur memory
               (if (zero? (get-val memory (+ cursor 1) base mode1))
                 (+ cursor 3)
                 (get-val memory (+ cursor 2) base mode2))
               base
               input
               output)

        :jump-if-false
        (recur memory
               (if (zero? (get-val memory (+ cursor 1) base mode1))
                 (get-val memory (+ cursor 2) base mode2)
                 (+ cursor 3))
               base
               input
               output)

        :less-than
        (recur (assoc memory
                      (idx memory (+ cursor 3) base mode3)
                      (if (< (get-val memory (+ cursor 1) base mode1)
                             (get-val memory (+ cursor 2) base mode2))
                        1
                        0))
               (+ cursor 4)
               base
               input
               output)

        :equals
        (recur (assoc memory
                      (idx memory (+ cursor 3) base mode3)
                      (if (= (get-val memory (+ cursor 1) base mode1)
                             (get-val memory (+ cursor 2) base mode2))
                        1
                        0))
               (+ cursor 4)
               base
               input
               output)
        :adjust-relative-base
        (recur memory
               (+ cursor 2)
               (+ base (get-val memory (+ cursor 1) base mode1))
               input
               output)))))
