(ns adventofcode2022.day12
  (:require [clojure.java.io :as io]))


(def START -1)
(def END (-> (int \z) (- (int \a)) inc))


(defn parse-input [filename]
  (->> (io/resource filename)
       io/reader
       line-seq
       (mapv (fn [line]
               (mapv #(case %
                       \S START
                       \E END
                       (-> (int %) (- (int \a))))
                     line)))))


(def test-input (parse-input "day12.test.txt"))
(def input (parse-input "day12.txt"))


(defn neighbours [m [row col]]
  (let [v (get-in m [row col])]
    (->> [[-1 0] [1 0] [0 -1] [0 1]]
      (map #(vector (+ row (second %)) (+ col (first %))))
      (filter #(and (get-in m %) 
                    (#{0 1} (abs (- (get-in m %) v))))))))


(defn find-node [m v]
  (->> (for [row (range (count m))
             col (range (count (first m)))
             :let [v' (get-in m [row col])]
             :when (= v v')]
         [row col])
       first))


(let [m           test-input
      start-node  (find-node m START)
      end-node    (find-node m END)]
  (loop [distances  {end-node 0}
         queue      [end-node]
         visited    #{}]
    (cond 
      (nil? queue)
      distances


      ; (= node end-node)
      ; (get distances end-node)

      :else 
      (let [distance'   (inc (distances node))
            queue'      (->> queue
                             (map (partial neighbours m node))
                             (filter #(not (visited %))))
            distances'  (->> neighbours'
                             (reduce (fn [ds n]
                                       (update ds n (fnil #(min % distance') Long/MAX_VALUE)))
                                     distances))
            nodes'      (->> (concat nodes neighbours')
                             (sort-by #(get distances' % Long/MAX_VALUE)))]
        (recur distances'
               nodes'
               (conj visited node)))
      )))



(let [m           test-input
      start-node  (find-node m START)
      end-node    (find-node m END)]
  (loop [[path & queue] (list (list start-node))
         visited        #{}]
    (cond 
      (nil? path)
      :not-found

      (= end-node (first path))
      (reverse path)

      :else 
      (let [node (first path)
            neighbours' (->> (neighbours m node)
                             (filter #(not (visited %))))
            visited' (into visited neighbours')
            queue' (->> neighbours'
                        (map #(conj path %))
                        (into queue))]
        (recur queue' visited')))))


(into (list 1 2) (list 3 4))