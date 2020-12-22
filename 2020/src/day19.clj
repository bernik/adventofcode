(ns day19
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))

(def input (->> "day19.edn"
                io/resource
                io/file
                slurp 
                read-string))

(def test-input-1
  {:rules ["0: 4 1 5"
           "1: 2 3 | 3 2"
           "2: 4 4 | 5 5"
           "3: 4 5 | 5 4"
           "4: a"
           "5: b"]
   :messages ["ababbb"
              "bababa"
              "abbbab"
              "aaabbb"
              "aaaabbb"]})

(def test-input-2
  {:rules ["42: 9 14 | 10 1"
           "9: 14 27 | 1 26"
           "10: 23 14 | 28 1"
           "1: a"
           "11: 42 31"
           "5: 1 14 | 15 1"
           "19: 14 1 | 14 14"
           "12: 24 14 | 19 1"
           "16: 15 1 | 14 14"
           "31: 14 17 | 1 13"
           "6: 14 14 | 1 14"
           "2: 1 24 | 14 4"
           "0: 8 11"
           "13: 14 3 | 1 12"
           "15: 1 | 14"
           "17: 14 2 | 1 7"
           "23: 25 1 | 22 14"
           "28: 16 1"
           "4: 1 1"
           "20: 14 14 | 1 15"
           "3: 5 14 | 16 1"
           "27: 1 6 | 14 18"
           "14: b"
           "21: 14 1 | 1 14"
           "25: 1 1 | 1 14"
           "22: 14 14"
           "8: 42"
           "26: 14 22 | 1 20"
           "18: 15 15"
           "7: 14 5 | 1 21"
           "24: 14 1"]
   :messages ["abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa"
              "bbabbbbaabaabba"
              "babbbbaabbbbbabbbbbbaabaaabaaa"
              "aaabbbbbbaaaabaababaabababbabaaabbababababaaa"
              "bbbbbbbaaaabbbbaaabbabaaa"
              "bbbababbbbaaaaaaaabbababaaababaabab"
              "ababaaaaaabaaab"
              "ababaaaaabbbaba"
              "baabbaaaabbaaaababbaababb"
              "abbbbabbbbaaaababbbbbbaaaababb"
              "aaaaabbaabaaaaababaa"
              "aaaabbaaaabbaaa"
              "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa"
              "babaaabbbaaabaababbaabababaaab"
              "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"]})

(defn parse-rules [lines]
  (->> lines
       (map #(str/split % #": "))
       (map (fn [[n line]]
              ;; (pprint line)
              (cond 
                (re-find #"\|" line)
                [n (mapv #(str/split % #" ") (str/split line #" \| "))]
                
                (re-find #"\d" line)
                [n (str/split line #" ")]
                
                :else 
                [n line])))
       (into (sorted-map))))


(def resolve-rule
  (memoize
   (fn [rules x part2?]
     (cond
       (and part2?
            (= "8" x))
       (str "(?:" (resolve-rule rules "42" part2?) ")+")

       (and part2? 
            (= "11" x))
       (let [r42 (resolve-rule rules "42" part2?)
             r31 (resolve-rule rules "31" part2?)
             inner (->> (range 1 11)
                        (map #(str "(?:"
                                   (apply str (repeat % r42))
                                   (apply str (repeat % r31))
                                   ")"))
                        (str/join "|"))
             ]
         (str "(?:" inner ")"))

       (vector? (first x))
       (str "(?:"
            (->> x
                 (map #(resolve-rule rules % part2?))
                 (str/join "|"))
            ")")

       (vector? x)
       (apply str (map #(resolve-rule rules % part2?) x))

       (#{"a" "b"} x)
       x

       (string? x)
       (resolve-rule rules (get rules x) part2?)

       :else nil)))
)

(defn part-1 [input]
  (let [re (re-pattern (str "^" (resolve-rule (parse-rules (:rules input)) "0" false) "$"))]
    (->> (:messages input)
         (filter #(re-find re %))
         count)))

(pprint (part-1 test-input-1))
(pprint (part-1 input))

(defn part-2 [input]
  (loop [rules (parse-rules (:rules input))
         messages (into #{} (:messages input))
         matches #{} ]
    (let [re (re-pattern (str "^" (resolve-rule rules "0" true) "$"))
          matches' (filter #(re-find re %) messages)]
      (if (empty? matches')
        (count matches)
        (recur rules
               (apply (partial disj messages) matches')
               (into matches matches'))))))

(pprint (part-2 test-input-2))
(pprint (part-2 input))


