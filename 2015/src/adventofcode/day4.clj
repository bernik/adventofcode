(ns adventofcode.day4
  (:import java.security.MessageDigest
           java.math.BigInteger))

(defn md5 [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        sig (.toString (BigInteger. 1 raw) 16)
        padding (apply str (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(def prefix "ckczppom")

(defn answer [hash-prefix] 
  (->> (range)
       (pmap #(list (md5 (str prefix %)) %))
       (filter #(-> % first (.startsWith hash-prefix)))
       first
       second))

(defn part-one []
  (answer "00000"))

(defn part-two []
  (answer "000000"))
