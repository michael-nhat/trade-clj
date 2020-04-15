(ns mercurius.trading.presentation.page
  (:require [mercurius.trading.presentation.tickers.panel :refer [tickers-panel]]
            [mercurius.trading.presentation.balances.panel :refer [balances-panel]]
            [mercurius.trading.presentation.order-book.panel :refer [order-book-panel]]
            [mercurius.trading.presentation.trades.panel :refer [trades-panel]]
            [mercurius.trading.presentation.place-order.form :refer [place-order-panel]]))

(defn trading-page []
  [:div.trading-page
   [:div.columns
    [:div.column.is-narrow
     [tickers-panel]]
    [:div.column.is-narrow
     [balances-panel]]
    [:div.column.is-narrow
     [place-order-panel]]]
   [:div.columns
    [:div.column.is-7 [order-book-panel]]
    [:div.column.is-5 [trades-panel]]]])
