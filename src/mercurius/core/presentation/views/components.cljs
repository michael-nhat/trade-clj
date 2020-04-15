(ns mercurius.core.presentation.views.components)

(defn page-loader []
  [:div.page-loader
   [:div.loader]])

(defn loader []
  [:div.loader])

(defn panel [{:keys [header subheader actions loading? class]} & body]
  [:div.panel {:class class}
   [:div.panel-heading
    [:div.level
     [:div.level-left
      [:div.level-item
       header
       (when subheader
         [:span.is-size-6.has-text-grey.m-l-sm subheader])]]
     (when (seq actions)
       [:div.level-right
        (into [:div.level-item] actions)])]]
   (if loading?
     [:div.panel-block.has-loader [loader]]
     (into [:div.panel-block] body))])

(defn input [{:keys [on-change value] :or {value ""} :as opts}]
  [:input.input
   (assoc opts
          :value value
          :on-change #(on-change (-> % .-target .-value)))])

(defn select [{:keys [collection on-change value] :or {value ""} :as opts}]
  (let [html-opts (dissoc opts :collection :on-change :value)]
    [:div.select html-opts
     [:select
      (assoc opts
             :value value
             :on-change #(on-change (-> % .-target .-value)))
      (for [[text value] collection]
        [:option {:key value :value value} text])]]))
