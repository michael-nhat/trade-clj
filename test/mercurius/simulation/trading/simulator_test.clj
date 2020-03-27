(ns mercurius.simulation.trading.simulator-test
  (:require [clojure.test :refer [deftest is]]
            [mercurius.support.fixtures :refer [with-system]]
            [mercurius.simulation.trading.simulator :refer [run-simulation]]))

(deftest ^:integration ^:slow run-simulation-test
  (with-system [{:keys [dispatch] :as system} {}]
    (let [n-traders 100
          n-orders-per-trader 2
          initial-price 250.0
          usd-funds 1000M
          btc-funds (bigdec (/ usd-funds initial-price))
          expected-monetary-base {"USD" (* n-traders usd-funds)
                                  "BTC" (* n-traders btc-funds)}]
      (time (run-simulation system
                            :tickers {"BTCUSD" {:initial-price initial-price :initial-funds usd-funds}}
                            :n-traders n-traders
                            :n-orders-per-trader n-orders-per-trader
                            :max-ms-between-orders 10))
      (is (= expected-monetary-base (dispatch :calculate-monetary-base {})))
      (dispatch :execute-trades {:ticker "BTCUSD"})
      (is (= expected-monetary-base (dispatch :calculate-monetary-base {}))))))