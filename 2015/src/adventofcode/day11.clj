(ns adventofcode.day11)

(defn next-char [c]
  (if (= c \z)
    \a
    (-> c byte inc char)))

(defn next-pass [p]
  (apply 
   str 
   (cond 
     (empty? p) ""
     (= (last p) \z) (concat (next-pass (drop-last p)) (list \a))
     :else (concat (drop-last p) (list (next-char (last p)))))))

(def all-passwords (iterate next-pass "hxbxwxba"))

(defn include-increasing-letters? [s]
  (->> s
       (map byte)
       (partition 3 1)
       (some (fn [[a b c]] (and (= (- b a) 1)
                                (= (- c b) 1))))))

(def without-bad-symbols? (partial re-find #"^[^oil]+$"))
(def with-two-pairs?  (partial re-find #"(.)\1.*((?!\1).)\2"))

(def filtered-passwords 
  (->> all-passwords
       (filter #(and 
                 (include-increasing-letters? %)
                 (without-bad-symbols? %)
                 (with-two-pairs? %)))))

(def part-one (first filtered-passwords))
(def part-two (second filtered-passwords))
