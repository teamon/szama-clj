(ns szama.views
  (:use [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.element]))

(defn layout [content]
  (html
    (html5
      [:html
        [:head
          [:title "Szama"]]
        [:body content]]
      )))

(defn delete-form [url]
  (form-to [:delete url]
    (submit-button "Delete")))

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
          (submit-button "Create new user")])]])

(defn entry-form)
