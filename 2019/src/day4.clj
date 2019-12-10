(ns day4)

;; part 1
(->> (range 156218 652528)
     (map str)
     (filter #(= (sort %) (seq %)))
     (filter #(re-matches #".*(\d)\1.*"  %))
     count
     println)

;; part 2
(->> (range 156218 652528)
     (map str)
     (filter #(= (sort %) (seq %)))
     (filter #(.contains (vals (frequencies %)) 2))
     count
     println)
