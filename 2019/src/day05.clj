(ns day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (into [] (map read-string (str/split (slurp (io/file (io/resource "day5.txt"))) #","))))

(defn get-val [state pos mode]
  (if (= mode "0")
    (get state (get state pos) :not-found)
    (get state pos :not-found)))

(defn parse-instruction [s]
  (let [[_ m3 m2 m1 op] (re-find #"(\d)(\d)(\d)(\d\d)" (format "%05d" s))]
    {:op    op
     :mode1 m1
     :mode2 m2
     :mode3 m3}))


(defn run-program [init-state input debug?]
  (loop [state init-state
         pos 0]
    (let [{:keys [op mode1 mode2 mode3]} (parse-instruction (get state pos))]
      (when debug?
        (println (list state 
                       pos 
                       op 
                       (get-val state (+ pos 1) "1")
                       (get-val state (+ pos 1) mode1)
                       (get-val state (+ pos 2) "1")
                       (get-val state (+ pos 2) mode2)
                       (get-val state (+ pos 3) "1")
                       (get-val state (+ pos 3) mode3))))
        
      (case op
        "99" (get state 0)
        ;; add 
        "01" (recur (assoc state (get state (+ pos 3))
                                 (+ (get-val state (+ pos 1) mode1) (get-val state (+ pos 2) mode2)))
                    (+ pos 4))
        ;; multiply 
        "02" (recur (assoc state (get state (+ pos 3))
                                 (* (get-val state (+ pos 1) mode1) (get-val state (+ pos 2) mode2)))
                    (+ pos 4))
        ;; read input
        "03" (recur (assoc state (get state (+ pos 1)) input)
                    (+ pos 2))
        ;; print 
        "04" (do
              (println (get-val state (+ pos 1) mode1))
              (recur state (+ pos 2)))
        ;; jump-if-true
        "05" (recur state 
                    (if (zero? (get-val state (+ pos 1) mode1))
                      (+ pos 3)
                      (get-val state (+ pos 2) mode2)))
        ;; jump-if-false
        "06" (recur state 
                    (if (zero? (get-val state (+ pos 1) mode1))
                      (get-val state (+ pos 2) mode2)
                      (+ pos 3)))
        ;; less than
        "07" (recur (assoc state (get state (+ pos 3))
                                 (if (< (get-val state (+ pos 1) mode1)
                                        (get-val state (+ pos 2) mode2))
                                   1
                                   0))
                    (+ pos 4)) 
        ;; equals 
        "08" (recur (assoc state (get state (+ pos 3))
                                 (if (= (get-val state (+ pos 1) mode1)
                                        (get-val state (+ pos 2) mode2))
                                   1
                                   0))
                    (+ pos 4))))))


; (println (run-program [1101 100 -1 4 0 99] 1))
; (run-program [3,9,8,9,10,9,4,9,99,-1,8] 8) ;; 1
; (run-program [3,9,8,9,10,9,4,9,99,-1,8] 1) ;; 0
; (run-program [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] 0 true) ;; 0
; (run-program [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] 2 true) ;; 1
; (run-program [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] 0 true) ;; 0
; (run-program [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] 3 true) ;; 1

;; part 1
(run-program input 1 false)
;; part 2
(run-program input 5 false)
