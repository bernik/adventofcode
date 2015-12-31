(ns adventofcode.day22)

(def boss {:hp 71
           :damage 10})

(def me {:hp 50
         :armor 0
         :mana 500})

(def spells {:missile {:cost 53 
                       :damage 4}
             :drain {:cost 73
                     :damage 2 
                     :heal 2}
             :shield {:cost 113 
                      :heal 7 
                      :turns 6}
             :poison {:cost 173
                      :damage 3
                      :turns 6}
             :recharge {:cost 229
                        :mana 101
                        :turns 5}})

(def init-state {:me me
                 :boss boss
                 :effects []
                 :spent-mana 0
                 :turn :me})

(defn apply-effect [state [spell timer]]
  (case spell
    :shield (update-in state [:me :hp] + 7)
    :poison (update-in state [:boss :hp] - 3)
    :recharge (update-in state [:me :mana] + 101)))

(defn winner? [{{me-hp :hp} :me {boss-hp :hp} :boss}] 
  (cond 
    (zero? me-hp) :boss
    (zero? boss-hp) :me))

(comment (winner? {:me {:hp 0} :boss {:hp 10}})
         (winner? {:me {:hp 10} :boss {:hp 0}})
         (winner? {:me {:hp 10} :boss {:hp 10}}))

(defn decrease-timers [state]
  (assoc state :effects (filter (comp pos? second) 
                                (map #(vector (first %) (dec (second %))) 
                                     (:effects state)))))

(comment (decrease-timers {:effects []})
         (decrease-timers {:effects [[:foo 1] [:bar 2]]})
         (decrease-timers {:effects [[:foo 3] [:bar 2]]}))

(defn cast-spell [state spell]
  state)

(defn my-turn [state]
  (let [available-spells (filter #(>= (get-in state [:me :mana])
                                      (:cost (second %))) 
                                 spells)]
    (cast-spell state (first available-spells))))

(defn make-turn [state]
  (assoc 
   (case (:turn state)
     :boss (update-in state [:me :hp] - (:damage boss))
     :me   (my-turn state))
   :turn (if (= (:turn state) :me)
           :boss
           :me)))

(comment (make-turn {:me {:hp 100} 
                     :boss {:hp 100} 
                     :turn :me})
         (make-turn {:me {:hp 100} 
                     :boss {:hp 100} 
                     :turn :boss}))

(comment 
  (loop [s init-state]
    (let [state (reduce apply-effect s (:effects s))
          winner (winner? state)]
      (case winner
        :me (:spent-mana state)
        :boss :loose
        (recur (make-turn (decrease-timers state)))))))
