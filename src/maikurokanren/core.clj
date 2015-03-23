(ns maikurokanren.core
  (:refer-clojure :exclude [== disj conj]))

(defn mk-var [c] {:id c})

(defn mk-var? [x] (map? x))

(defn mk-var=? [x1 x2] (= (:id x1) (:id x2)))

(defn walk
  [u sub-map]
  (let [pr (and (mk-var? u) (get sub-map u))]
    (if pr
      (walk pr sub-map)
      u)))

(defn ext-s
  [mk-var val sub-map]
  (assoc sub-map mk-var val))

(defn unit
  [s]
  [s])


(defn make-stream
  [state])

(defn unify
  [u v s]
  (let [u (walk u s)
        v (walk v s)]
    (cond
     (and (mk-var? u) (mk-var? v) (mk-var=? u v)) s
     (mk-var? u) (ext-s u v s)
     (mk-var? v) (ext-s v u s)
     (and (sequential? u) (not-empty u) (sequential? v) (not-empty v))
          (let [s' (unify (first u) (first v) s)]
            (and s' (unify (rest u) (rest v) s')))
     :else (and (= u v) s))))

(defn ==
  [u v]
  (fn [s]
    (let [s' (unify u v (:sub-map s))]
      (if s'
        (unit (assoc s :sub-map s'))
        []))))

(defn call-fresh
  [f]
  (fn [s]
    (let [c (:counter s)
          fg (f (mk-var  c))]
      (fg (assoc s :counter (+ 1 c))))))

(defn mplus
  [s1 s2]
  (cond
   (empty? s1) s2
   :else
   (vec (cons (first s1) (mplus (rest s1) s2)))))

(defn bind
  [s g]
  (cond
   (empty? s) (mzero)
   :else
   (mplus (g (first s)) (bind (rest s) g))))

(defn disj
  [g1 g2]
  (fn [s]
    (mplus (g1 s) (g2 s))))

(defn conj
  [g1 g2]
  (fn [s]
    (bind (g1 s) g2)))

(def empty-state {:counter 0 :sub-map {}})

((conj
  (call-fresh (fn [a] (== a 7)))
  (call-fresh (fn [b] (disj (== b 6) (== b 5))))) empty-state)
