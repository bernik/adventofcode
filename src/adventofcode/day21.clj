(ns adventofcode.day21
  (:require [clojure.math.combinatorics :as combo]))

(def boss {:hp 103
           :damage 9
           :armor 2})

(def me {:hp 100
         :damage 0
         :armor 0
         :cost 0})

(def shop {:weapons [{:name "dagger" :cost 8 :damage 4 :armor 0}
                     {:name "shortsword" :cost 10 :damage 5 :armor 0}
                     {:name "warhammer" :cost 25 :damage 6 :armor 0}
                     {:name "longsword" :cost 40 :damage 7 :armor 0}
                     {:name "greataxe" :cost 74 :damage 8 :armor 0}]
           :armor [{:name "shirt" :cost 0 :damage 0 :armor 0}
                   {:name "leather" :cost 13 :damage 0 :armor 1}
                   {:name "chainmail" :cost 31 :damage 0 :armor 2}
                   {:name "splintmail" :cost 53 :damage 0 :armor 3} 
                   {:name "bandedmail" :cost 75 :damage 0 :armor 4}
                   {:name "platemail" :cost 102 :damage 0 :armor 5}]
           :rings [{:name "damage +1" :cost 25 :damage 1 :armor 0}
                   {:name "damage +2" :cost 50 :damage 2 :armor 0}
                   {:name "damage +3" :cost 100 :damage 3 :armor 0}
                   {:name "defence +1" :cost 20 :damage 0 :armor 1}
                   {:name "defence +2" :cost 40 :damage 0 :armor 2}
                   {:name "defence +3" :cost 80 :damage 0 :armor 3}]})

(defn win? [me boss]
  (<= (/ (:hp boss) (max 1 (- (:damage me) (:armor boss))))
      (/ (:hp me) (max 1 (- (:damage boss) (:armor me)))))) 

(defn win-2? [me boss]
  (let [my-damage (max 1 (- (:damage me) (:armor boss)))
        boss-damage (max 1 (- (:damage boss) (:armor me)))]
    (loop [my-hp (:hp me)
           boss-hp (:hp boss)]
      (if (> 0 (- boss-hp my-damage))
        true 
        (if (> 0 (- my-hp boss-damage))
          false
          (recur (- my-hp boss-damage) (- boss-hp my-damage)))))))

(comment (win-2? {:hp 8 :damage 5 :armor 5}
                 {:hp 12 :damage 7 :armor 2}))

(comment (win-2? {:hp 100 :damage 7 :armor 4}
                 boss))

(defn apply-items [items]
  (reduce #(-> %1 
               (update :cost + (:cost %2))
               (update :damage + (:damage %2))
               (update :armor + (:armor %2))) 
          me 
          items))

(comment (apply-items [{:cost 10 :damage 10 :armor 10}
                       {:cost 1 :damage 1 :armor 1}]))

(comment (->> 
          (for [weapon (:weapons shop)
                armor (:armor shop)
                rings (filter #(<= (count %) 2) (combo/subsets (:rings shop)))
                :let [items (concat [weapon armor] rings)
                      new-me (apply-items items)]
                :when (win-2? new-me boss)] 
            [new-me items])
          (sort-by (comp :cost first))
          first
          clojure.pprint/pprint
          #_(map :cost)
          #_(apply min)))
