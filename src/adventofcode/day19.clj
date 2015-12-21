(ns adventofcode.day19
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def molecule "CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThCaCaPTiRnPBSiThRnFArArCaCaSiThCaSiThSiRnMgArCaPTiBPRnFArSiThCaSiRnFArBCaSiRnCaPRnFArPMgYCaFArCaPTiTiTiBPBSiThCaPTiBPBSiRnFArBPBSiRnCaFArBPRnSiRnFArRnSiRnBFArCaFArCaCaCaSiThSiThCaCaPBPTiTiRnFArCaPTiBSiAlArPBCaCaCaCaCaSiRnMgArCaSiThFArThCaSiThCaSiRnCaFYCaSiRnFYFArFArCaSiRnFYFArCaSiRnBPMgArSiThPRnFArCaSiRnFArTiRnSiRnFYFArCaSiRnBFArCaSiRnTiMgArSiThCaSiThCaFArPRnFArSiRnFArTiTiTiTiBCaCaSiRnCaCaFYFArSiThCaPTiBPTiBCaSiThSiRnMgArCaF") 

(def replacements (->> "day19.replacements.txt"
                       io/resource 
                       io/file 
                       slurp
                       str/split-lines
                       (map #(rest (re-find #"(\w+) => (\w+)" %)))))

(defn indexes [s needle]
  (loop [haystack s
         res []]
    (let [index (.indexOf haystack needle)
          total-index (if (empty? res) 0 (inc (last res)))]
      (if (= index -1)
        res
        (recur (subs haystack (inc index))
               (conj res (+ total-index index)))))))

(defn apply-replacement [molecule [from to]]
  (map #(str (subs molecule 0 %)
             to 
             (subs molecule (+ % (count from))))
       (indexes molecule from)))

(def part-one (->> replacements 
                   (mapcat #(apply-replacement molecule %))
                   (into #{})
                   count))
