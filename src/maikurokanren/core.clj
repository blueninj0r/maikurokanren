(ns maikurokanren.core
  (:use [clojure.repl :only (source)])
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
  (fn [substitution]
    (let [updated-sub-map (unify u v (:sub-map substitution))]
      (if updated-sub-map
        (unit (assoc substitution :sub-map updated-sub-map))
        []))))

(defn call-fresh
  [f]
  (fn [substituion]
    (let [counter (:counter substituion)
          fg (f (mk-var  counter))]
      (fg (assoc substituion :counter (+ 1 counter))))))

(defn mplus
  [stream1 stream2]
  (cond
   (empty? stream1) stream2
   (fn? stream1) (fn [] (mplus stream2 (stream1)))
   :else
   (vec (cons (first stream1) (mplus (rest stream1) stream2)))))

(defn bind
  [stream goal]
  (cond
   (empty? stream) (vector)
   (fn? stream) (fn [] (bind (stream) goal))
   :else
   (mplus (goal (first stream)) (bind (rest stream) goal))))

(defn disj
  [goal1 goal2]
  (fn [stream]
    (mplus (goal1 stream) (goal2 stream))))

(defn conj
  [goal1 goal2]
  (fn [stream]
    (bind (goal1 stream) goal2)))

(def empty-state {:counter 0 :sub-map {}})


((disj
  (call-fresh (fn [a] (== a 7)))
  (call-fresh (fn [b] (disj (== b 6) (== b 5))))) empty-state)

((call-fresh (fn [b] (disj (== b 6) (== b 5)))) empty-state)

[{:counter 2, :sub-map {{:id 1} 6, {:id 0} 7}}
 {:counter 2, :sub-map {{:id 1} 5, {:id 0} 7}}]

(defn fives
  [x]
  (disj (== x 5) (fn [sc] (fn [] ((fives x) sc)))))

((call-fresh fives) empty-state)
