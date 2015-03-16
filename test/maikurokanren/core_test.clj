(ns maikurokanren.core.test
  (:require [maikurokanren.core :as mk])
  (:use clojure.test))

(is (= (mk/mk-var 1) #{1}))

(is (true? (mk/mk-var? (mk/mk-var 1))))

(is (true? (mk/mk-var=? (mk/mk-var 1) (mk/mk-var 1))))

(is (false? (mk/mk-var=? (mk/mk-var 1) (mk/mk-var 2))))
