(ns intcode-computer)

(def debug? true)
(def debug? false)

(defn get-val [memory cursor mode]
  (if (= mode "0")
    (get memory (get memory cursor) :not-found)
    (get memory cursor :not-found)))

(def instruction-names {"99" :halt
                        "01" :add 
                        "02" :multiply
                        "03" :read-input  
                        "04" :print  
                        "05" :jump-if-true
                        "06" :jump-if-false 
                        "07" :less-than 
                        "08" :equals})

(defn parse-instruction [s]
  (let [[_ m3 m2 m1 op] (re-find #"(\d)(\d)(\d)(\d\d)" (format "%05d" s))]
    {:op    (instruction-names op)
     :mode1 m1
     :mode2 m2
     :mode3 m3}))


(defn run [memory cursor & input]
  (loop [memory memory
         cursor cursor 
         input input
         output nil]
    (let [{:keys [op mode1 mode2 mode3]} (parse-instruction (get memory cursor))]
      (when debug?
        (println (list memory 
                       cursor 
                       op 
                       (get-val memory (+ cursor 1) "1")
                       (get-val memory (+ cursor 1) mode1)
                       (get-val memory (+ cursor 2) "1")
                       (get-val memory (+ cursor 2) mode2)
                       (get-val memory (+ cursor 3) "1")
                       (get-val memory (+ cursor 3) mode3))))
        
      (case op
        :halt
        [:exit (or output (get memory 0))]

        :add
        (recur (assoc memory
                      (get memory (+ cursor 3))
                      (+ (get-val memory (+ cursor 1) mode1)
                         (get-val memory (+ cursor 2) mode2)))
               (+ cursor 4)
               input
               output)

        :multiply
        (recur (assoc memory
                      (get memory (+ cursor 3))
                      (* (get-val memory (+ cursor 1) mode1) 
                         (get-val memory (+ cursor 2) mode2)))
               (+ cursor 4)
               input
               output)

        :read-input
        (if (seq input)
          (recur (assoc memory
                        (get memory (+ cursor 1))
                        (first input))
                 (+ cursor 2)
                 (rest input)
                 output)
          [:pending output memory cursor])

        :print
        (recur memory 
               (+ cursor 2) 
               input 
               (get-val memory (+ cursor 1) mode1))

        :jump-if-true
        (recur memory
               (if (zero? (get-val memory (+ cursor 1) mode1))
                 (+ cursor 3)
                 (get-val memory (+ cursor 2) mode2))
               input
               output)

        :jump-if-false
        (recur memory
               (if (zero? (get-val memory (+ cursor 1) mode1))
                 (get-val memory (+ cursor 2) mode2)
                 (+ cursor 3))
               input
               output)

        :less-than
        (recur (assoc memory
                      (get memory (+ cursor 3))
                      (if (< (get-val memory (+ cursor 1) mode1)
                             (get-val memory (+ cursor 2) mode2))
                        1
                        0))
               (+ cursor 4)
               input
               output)

        :equals
        (recur (assoc memory 
                      (get memory (+ cursor 3))
                      (if (= (get-val memory (+ cursor 1) mode1)
                             (get-val memory (+ cursor 2) mode2))
                        1
                        0))
               (+ cursor 4)
               input
               output)))))
