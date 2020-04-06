(ns mercurius.support.helpers
  (:require [clojure.core.async :refer [timeout alts!!]]
            [clojure.spec.test.alpha :as stest]
            [mercurius.core.configuration.system :refer [start stop]]))

(defn check-specs
  "Kaocha hook to check fdef specs."
  [test _test-plan]
  (stest/instrument)
  test)

(defn recorded-calls [calls-chan expected-count & {:keys [wait-for] :or {wait-for 500}}]
  (loop [recorded []
         actual-count 0]
    (if (< actual-count expected-count)
      (let [[call _] (alts!! [calls-chan (timeout wait-for)])]
        (if call
          (recur (conj recorded call) (inc actual-count))
          recorded))
      recorded)))

(defn ref-trx [f]
  (dosync
   (f)))

(defmacro with-system
  "Starts the system and makes sure it's stopped afterward.
  The bindings works like let where the second argument are passed to start (not implemented yet)."
  [bindings & body]
  `(let [system# (start ~(last bindings))]
     (try
       (let [~(first bindings) system#]
         ~@body)
       (finally
         (stop system#)))))
