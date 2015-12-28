(ns adventofcode.day22)

(def boss {:hp 71
           :damage 10})

(def me {:hp 50
         :mana 500})

(def spells [{:name :magic-missile
              :cost 53 
              :damage 4}
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
