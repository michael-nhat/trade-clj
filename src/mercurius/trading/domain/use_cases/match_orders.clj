(ns mercurius.trading.domain.use-cases.match-orders
  (:require [clojure.spec.alpha :as s]
            [mercurius.trading.domain.entities.order :as order :refer [amount-delivered currency-delivered]]
            [mercurius.trading.domain.entities.trade :refer [generate-trade]]
            [mercurius.wallets.domain.entities.wallet :refer [transfer cancel-reservation]]))

(s/def ::bid ::order/order)
(s/def ::ask ::order/order)
(s/def ::command (s/and (s/keys :req-un [::bid ::ask])
                        #(= (get-in % [:bid :ticker])
                            (get-in % [:ask :ticker]))))

(defn- make-transfer [fetch-wallet load-wallet save-wallet trade
                      from-user to-user {:keys [side ticker] :as order-reserved}]
  (let [amount-to-cancel (amount-delivered side (:amount trade) (:price order-reserved))
        amount-to-transfer (amount-delivered side (:amount trade) (:price trade))
        currency (currency-delivered side ticker)
        src (-> (fetch-wallet from-user currency)
                (cancel-reservation amount-to-cancel))
        dst (load-wallet to-user currency)
        wallets (transfer src dst amount-to-transfer)]
    (doseq [wallet wallets]
      (save-wallet wallet))))

(defn new-match-orders-use-case
  "Returns a use case that match bid an ask orders to see if a trade can be made.
  If a trade is made, a transfer is made between buyer and seller for each pais's currency."
  [{:keys [fetch-wallet load-wallet save-wallet]}]
  (fn [{:keys [bid ask] :as command}]
    (s/assert ::command command)
    (when-let [trade (generate-trade bid ask)]
      (let [[buyer seller] (map :user-id [bid ask])
            make-transfer (partial make-transfer fetch-wallet load-wallet save-wallet trade)]
        (make-transfer buyer seller bid)
        (make-transfer seller buyer ask)
        trade))))
