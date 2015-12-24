(ns adventofcode.day22
  (:require [clojure.math.combinatorics :as combo]))

(def boss {:hp 71
           :damage 10})

(def me {:hp 50
         :mana 500})

(def spells [{:name :magic-missile
              :cost 53 
              :damage 2}
             {:name :drain
              :cost 73
              :damage 2 
              :heal 2}
             {:name :shield
              :cost 113 
              :heal 7 
              :turns 6}
             {:name :poison 
              :cost 173
              :damage 3
              :turns 6}
             {:name :recharge
              :cost 229
              :mana 101
              :turns 5}])

(defn total-damage [spells]
  (apply + (map #(* (get % :damage 0) (get % :turns 1)) spells)))

(defn total-heal [spells]
  (apply + (map #(* (get % :heal 0) (get % :turns 1)) spells))) 

(defn total-cost [spells]
  (- (apply + (map #(* (get % :mana 0) (get % :turns 1)) spells))
     (apply + (map :cost spells))))  

(defn total-spend-mana [spells]
  (apply + (map #(get % :cost) spells)))

(comment 
  (wizard {:hp 10 :mana 250} [3 0])
  (total-damage (map #(get spells %) [3 0])) 
  (total-heal (map #(get spells %) [3 0])) 
  (total-cost (map #(get spells %) [3 0]))
  (total-spend-mana (map #(get spells %) [3 0])))

(defn wizard [me spell-indexes]
  (let [spells (map #(get spells %) spell-indexes)]
    (-> me 
        (update :hp + (total-heal spells))
        (update :mana + (total-cost spells))
        (assoc :damage (total-damage spells)))))

(defn win? [me boss spell-indexes]  
  (let [wizard (wizard me spell-indexes)
        boss-damage (* (:damage boss) (dec (count spell-indexes)))] 
    (and (>= (:mana wizard) 0)
         (>= (- (:hp wizard) boss-damage)
             (- (:hp boss) (:damage wizard))))))

(defn min-win [spells-count]
  (->> (combo/selections (range 0 5) spells-count)
       (filter #(win? me boss %))
       (map #(get spells %))
       (map total-spend-mana)
       (apply min)))
