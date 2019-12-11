(ns intcode-computer)

(def state (atom []))

(def debug? false)

(defn get-val [pos mode]
  (if (= mode "0")
    (get @state (get @state pos) :not-found)
    (get @state pos :not-found)))

(defn parse-instruction [s]
  (let [[_ m3 m2 m1 op] (re-find #"(\d)(\d)(\d)(\d\d)" (format "%05d" s))]
    {:op    op
     :mode1 m1
     :mode2 m2
     :mode3 m3}))


(defn run [init-state & input]
  (reset! state init-state)
  (loop [pos 0 
         input input
         output nil]
    (let [{:keys [op mode1 mode2 mode3]} (parse-instruction (get @state pos))]
      (when debug?
        (println (list @state 
                       pos 
                       op 
                       (get-val (+ pos 1) "1")
                       (get-val (+ pos 1) mode1)
                       (get-val (+ pos 2) "1")
                       (get-val (+ pos 2) mode2)
                       (get-val (+ pos 3) "1")
                       (get-val (+ pos 3) mode3))))
        
      (case op
        "99"  ;; halt
        (or output (get @state 0))

        "01" ;; add 
        (do 
          (swap! state assoc (get @state (+ pos 3))
                             (+ (get-val (+ pos 1) mode1) (get-val (+ pos 2) mode2))) 
          (recur (+ pos 4) input output))

        "02" ;; multiply
        (do 
          (swap! state assoc (get @state (+ pos 3))
                             (* (get-val (+ pos 1) mode1) (get-val (+ pos 2) mode2)))
          (recur (+ pos 4) input output))

        "03" ;; read input  
        (do 
          (swap! state assoc (get @state (+ pos 1)) (first input))
          (recur (+ pos 2) (rest input) output))

        "04" ;; print  
        (do
          (println (get-val (+ pos 1) mode1))
          (recur (+ pos 2) input output))

        "05" ;; jump if true
        (recur 
          (if (zero? (get-val (+ pos 1) mode1))
              (+ pos 3)
              (get-val (+ pos 2) mode2))
          input
          output)

        "06" ;; jump if false 
        (recur
          (if (zero? (get-val (+ pos 1) mode1))
              (get-val (+ pos 2) mode2)
              (+ pos 3))
          input
          output)

        "07" ;; less than 
        (do 
          (swap! state assoc 
                      (get @state (+ pos 3))
                      (if (< (get-val (+ pos 1) mode1)
                             (get-val (+ pos 2) mode2))
                        1
                        0))
          (recur (+ pos 4) input output))

        "08" ;; equals 
        (do 
          (swap! state assoc (get @state (+ pos 3))
                             (if (= (get-val (+ pos 1) mode1)
                                    (get-val (+ pos 2) mode2))
                               1
                               0))
          (recur (+ pos 4) input output))))))
