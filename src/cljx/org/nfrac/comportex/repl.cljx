(ns org.nfrac.comportex.repl
  "Optional REPL tweaks"
  (:require #+clj [clojure.pprint :as pprint]
            [org.nfrac.comportex.cells
             #+cljs :refer #+cljs [LayerOfCells]]
            [org.nfrac.comportex.synapses
             #+cljs :refer #+cljs [SynapseGraph]])
  #+clj
  (:import [org.nfrac.comportex.cells LayerOfCells]
           [org.nfrac.comportex.synapses SynapseGraph]))

;;; ## Truncate large data structures

(defn patchmethod1
  "Transform the first argument of calls to `multifn` before calling the method
  currently associated with dispatch-value. This transform happens after
  dispatch."
  [multifn dispatch-val f]
  (let [dispatch-fn (get-method multifn dispatch-val)]
    (defmethod multifn dispatch-val
      [arg1 & more]
      (apply dispatch-fn (f arg1) more))))

(def ^:dynamic *truncated-print-length* 3)

(def print-methods [#+clj print-method
                    #+clj pprint/simple-dispatch])

(def should-truncate {LayerOfCells
                      [:boosts :active-duty-cycles :overlap-duty-cycles],
                      SynapseGraph
                      [:syns-by-target :targets-by-source]})

(defrecord TruncateOnPrint [v])

(defn truncate-large-data-structures []
  ;; The TruncateOnPrint is invisible. Its contents are visible, but truncated.
  (doseq [m print-methods]
    (defmethod m TruncateOnPrint
      [this & args]
      (binding [*print-length* (if (and *print-length*
                                        *truncated-print-length*)
                                 (min *print-length*
                                      *truncated-print-length*)
                                 (or *print-length*
                                     *truncated-print-length*))]
        (apply m (:v this) args))))

  ;; Before printing records, wrap the specified fields in a TruncateOnPrint.
  (doseq [m print-methods
          [recordclass noisykeys] should-truncate]
    (patchmethod1 m recordclass
                  (fn [this]
                    (reduce #(update-in % [%2] ->TruncateOnPrint)
                            this noisykeys)))))
