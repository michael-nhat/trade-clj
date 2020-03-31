(ns mercurius.support.factory
  (:require [mercurius.wallets.domain.entities.wallet :refer [new-wallet]]
            [mercurius.trading.domain.entities.order :refer [new-order]]
            [mercurius.trading.domain.entities.trade :refer [new-trade]]))

;;;; Helpers

(def last-id (atom 0))
(defn- next-id! []
  (swap! last-id inc))

(defn- build-with [entity-fn defaults-fn]
  (fn [& [args]]
    (let [defaults (if (fn? defaults-fn)
                     (defaults-fn args)
                     defaults-fn)]
      (entity-fn (merge defaults args)))))

;;;; Factories 

(def build-user-id next-id!)

(def build-wallet
  (build-with new-wallet (fn [_] {:currency "USD" :user-id (build-user-id)})))

(def build-order
  (build-with new-order
              (fn [_]
                {:user-id (build-user-id)
                 :type :limit
                 :side :buy
                 :amount 0.2M
                 :ticker "BTCUSD"
                 :price 100.0})))

(def build-trade
  (build-with new-trade {:ticker "BTCUSD" :amount 1M :price 100.0}))

(comment
  (build-wallet)
  (build-wallet {:balance 5})
  (build-order))
