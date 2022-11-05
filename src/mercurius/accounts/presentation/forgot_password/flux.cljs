(ns mercurius.accounts.presentation.forgot_password.flux
  (:require [re-frame.core :refer [reg-sub reg-event-fx]]
            [mercurius.core.presentation.util.reframe :refer [reg-event-db]]
            [mercurius.core.presentation.util.meta :refer [csrf-token]]
            [day8.re-frame.http-fx]
            [ajax.edn :as edn]))

(def default-forgot-password-form {:loading? false :values {:username "" :password ""}})

;;;; Subscriptions

(reg-sub
 :forgot-password?
 (fn [db _]
   (:forgot-password db)
   false))

;;;; Events

(reg-event-db
 :forgot-password
 (fn [db [_ forgot-password?]]
   (assoc db :forgot-password forgot-password?)))


;;; fx

;; (reg-event-fx
;;  :forgot-password
;;  (fn [{:keys [db]} [_]]
;;    {:db (assoc db :forgot-password false)}))
