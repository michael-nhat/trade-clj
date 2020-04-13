(ns mercurius.accounts.presentation.views.login
  (:require [reagent.core :as r]
            [mercurius.core.presentation.util.reframe :refer [>evt <sub]]
            [mercurius.core.presentation.views.components :refer [input]]))

(defn- login-form []
  (let [credentials (r/atom {:username "" :password ""})
        on-submit (fn [ev]
                    (>evt [:login @credentials])
                    (.preventDefault ev))]
    (fn []
      (let [{:keys [loading?]} (<sub [:auth])]
        [:form {:on-submit on-submit}
         [:div.field
          [:div.control.has-icons-left
           [input credentials :username {:placeholder "Username" :auto-focus true}]
           [:span.icon.is-small.is-left
            [:i.fas.fa-user]]]]
         [:div.field
          [:div.control.has-icons-left
           [input credentials :password {:type "password" :placeholder "Password"}]
           [:span.icon.is-small.is-left
            [:i.fas.fa-lock]]]]
         [:div.field
          [:button.button.is-fullwidth.is-primary
           {:type "submit" :class (when loading? "is-loading")}
           "ENTER"]]]))))

(defn login-page []
  [:section.hero.is-dark.is-bold.is-fullheight
   [:div.hero-head
    [:nav.navbar
     [:div.navbar-brand
      [:div.navbar-item.is-uppercase.has-text-weight-bold "Mercurius"]]]]
   [:div.hero-body.has-text-centered
    [:div.container
     [:h1.title "Sign In"]
     [:h2.subtitle "Enter your credentials"]
     [:div.columns.is-centered
      [:div.column.is-4.has-text-dark
       [login-form]]]]]
   [:div.hero-foot
    [:div.navbar
     [:div.navbar-end
      [:div.navbar-item "Developed by "
       [:a.m-l-xs {:href "https://github.com/eeng"} "eeng"]]]]]])
