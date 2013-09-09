(ns szama.views
  (:use [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.element]
        [korma.core]
        [szama.db]))

; helpers
(defn delete-form [url]
  (form-to [:delete url]
    (submit-button "Delete")))

; templates
(defn layout [content]
  (html
    (html5
      [:html
        [:head
          [:title "Szama"]]
        [:body content]]
      )))

(defn user-select [name]
  [:select {:name name}
    [:option {:value ""} ""]
    (map (fn [u] [:option {:value (u :id)} (u :name)]) (select users))])

(defn order-form-table [n key header]
  [:div
    [:h3 header]
    [:table
      [:tr
        [:th "User"]
        [:th "Amount"]]
      (map (fn [i]
        [:tr
          [:td (user-select (str key "[" i "][user_id]"))]
          [:td (text-field (str key "[" i "][amount]"))]])
        (take n (range)))]])

(defn order-form []
  (form-to [:post "/orders"]
    (order-form-table 10 "eaters" "Eaters")
    [:div
      (label "delivery" "Delivery")
      (text-field "delivery")]
    (order-form-table 3 "payers" "Payers")
    [:div
      (submit-button "Create order")]))

(defn home []
  [:div
    (order-form)])

(defn users-index [users]
  [:div
    [:div
      [:table
        [:tr
          [:th "Name"]
          [:th "Delete"]]
        (map (fn [a]
          [:tr
            [:td (a :name)]
            [:td (delete-form (str "/users/" (a :id)))]])
          users)]]
    [:div
      (form-to [:post "/users"]
        [:div
          (label "name" "Name")
          (text-field "name")]
        [:div
          (submit-button "Create user")])]])
