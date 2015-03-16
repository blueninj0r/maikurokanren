(ns maikurokanren.core
  (:refer-clojure :exclude [==]))

(defn mk-var [c] [c])

(defn mk-var? [x] (vector? x))

(defn mk-var=? [x1 x2] (= (first x1) (first x2)))

(defn walk
  [u s]
  (let [pr (and (mk-var? u) (first (filter #(mk-var=? u %1) s)))]
    (if pr
      (walk (rest pr) s)
      u)))

(defn ext-s
  [x v s]
  (conj s [x v]))
