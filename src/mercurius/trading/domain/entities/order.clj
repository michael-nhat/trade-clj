(ns mercurius.trading.domain.entities.order
  (:require [clojure.spec.alpha :as s]
            [mercurius.util.uuid :refer [uuid]]
            [mercurius.trading.domain.entities.ticker :as ticker :refer [first-currency last-currency]]
            [mercurius.accounts.domain.entities.user :as user]
            [tick.alpha.api :as t]))

(s/def ::id string?)
(s/def ::user-id ::user/id)
(s/def ::type #{:market :limit})
(s/def ::side #{:buy :sell})
(s/def ::ticker ::ticker/ticker)
(s/def ::amount (s/and number? pos?))
(s/def ::price (s/and number? pos?))
(s/def ::remaining number?)
(s/def ::order (s/keys :req-un [::id ::user-id ::type ::side ::ticker ::amount ::price ::placed-at ::remaining]))

(defrecord Order [id user-id type side ticker amount price placed-at remaining])

(defn new-order [{:keys [amount] :as fields}]
  (let [defaults {:id (uuid) :placed-at (t/now) :remaining amount}]
    (->> fields (merge defaults) map->Order)))

(defn currency-delivered
  "Returns the pair's currency that is delivered when making a trade. It's also used for reservations.
  I.e., when buying BTCUSD, we should reserve USD. When selling instead, we should reserve BTC."
  [side ticker]
  (s/assert ::ticker/ticker ticker)
  (case side
    :buy (last-currency ticker)
    :sell (first-currency ticker)))

(defn amount-delivered
  "Calculates the amount to deliver to the other party. It's also used for reservations.
  E.g., when buying 0.2 BTCUSD at a price of 1000, we should reserve 100 USD.
  but when selling, 0.2 BTC should be reserved."
  [side amount price]
  (case side
    :buy (* amount price)
    :sell amount))

(defn calculate-reservation
  "Calculates the amount and currency to reserve for an order."
  [{:keys [side amount ticker price]}]
  {:amount (amount-delivered side amount price)
   :currency (currency-delivered side ticker)})

(defn fill-order [{:keys [remaining] :as order} fill-amount]
  {:pre [(<= fill-amount remaining)]}
  (update order :remaining - fill-amount))

(defn status? [{:keys [amount remaining]}]
  (cond
    (= remaining amount) :pending
    (zero? remaining) :completely-filled
    (< remaining amount) :partially-filled))

(defn partially-filled? [order]
  (= (status? order) :partially-filled))
