(ns szama.handler
  (:use [compojure.core]
        [korma.core]
        [korma.db]
        [szama.db]
        [ring.middleware.params]
        [ring.util.response]
        [clojure.string :only [blank?]])
  (:require [szama.views :as views]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn layout [tpl] (views/layout tpl))

(defn home [req] (layout (views/home)))

(defn users-index [req]
  (layout (views/users-index (select users))))

(defn users-create [req]
  (let [name ((req :params) :name)]
    (insert users (values {:name name}))
    (redirect "/users")))

(defn users-destroy [id]
  (delete users (where {:id (read-string id)} ))
  (redirect "/users"))

(defn string-to-number [s]
  (if (blank? s)
      0
      (read-string (clojure.string/replace s "," "."))))

(defn string-to-amount [s] (int (* 100 (string-to-number s))))

(defn save-entries [order mod items]
  (map
    (fn [e] (assoc e :amount (* (e :amount) mod)))
    (filter
      #(> (% :amount) 0)
      (map
        (fn [[k v]] {:user_id   (string-to-number (v :user_id))
                     :amount    (string-to-amount (v :amount))
                     :order_id  (order :id)})
        items))))

(defn orders-create [req]
  (transaction
    (let [params (req :params)
          eaters (params :eaters)
          payers (params :payers)
          delivery (string-to-amount (params :delivery))
          order (insert orders (values {:delivery delivery}))]
      (insert entries
        (values
          (concat
            (save-entries order -1 eaters)
            (save-entries order 1  payers)))))
  (redirect "/")))

(defroutes app-routes
  (context "/users" [] (defroutes users-routes
    (context "/:id" [id] (defroutes user-routes
      (DELETE "/" [] (users-destroy id))))
    (GET  "/" [] users-index)
    (POST "/" [] users-create)))

  (context "/orders" [] (defroutes orders-routes
    (POST "/" [] orders-create)))

  (GET "/" [] home)

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
    (wrap-params)
    ))
