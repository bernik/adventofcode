(ns day20
  (:require [clojure.java.io :as    io]
            [clojure.string  :as    str]
            [clojure.pprint  :refer [pprint]]))

(defn parse-tiles [tiles]
  (map (fn [tile]
         (let [[title-row & rows] (str/split tile #"\n")
               [_ title] (re-find #"(\d+)" title-row)]
           {:title title
            :rows (mapv #(into [] (seq %)) rows)}))
       tiles))

(def test-input (-> "day20.test.txt"
                    io/resource
                    io/file
                    slurp
                    (str/split #"\n\n")
                    parse-tiles))

(pprint test-input)
