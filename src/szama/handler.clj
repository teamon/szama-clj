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
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]))



(defn layout [tpl] (views/layout tpl))


(defn total-amount [order]
  (let [entries (order :entries)]
    (+ (order :delivery) (- (reduce +
            (filter #(< % 0)
                    (map #(% :amount) entries)))))))

(defn make-order [order]
  { :id (order :id)
    :created_at (order :created_at)
    :total (total-amount order)})

(defn home [req]
  (let [orders (map make-order
                    (select orders
                       (with entries)
                       (order :created_at :DESC)
                       (limit 5)))
        users (select users)]
    (layout (views/home orders users))))

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

(defn prepare-entries [order mod items]
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
          order (insert orders (values {:delivery delivery}))
          eaters-data (prepare-entries order -1 eaters)
          payers-data (prepare-entries order  1 payers)
          data (seq (concat eaters-data payers-data))]
      ; create entries records
      (insert entries (values data))

      ; update users balance
      (doall
        (map (fn [e]
          (exec-raw ["UPDATE users SET balance = balance + ? WHERE id = ?" [(e :amount) (e :user_id)]]))
        data)))

   (redirect "/")
   ))


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

(defn start [port]
  (jetty/run-jetty #'app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
               (or (System/getenv "PORT") "8080"))]
  (start port)))
