(ns szama.handler
  (:use [compojure.core]
        [korma.core]
        [szama.db]
        [ring.middleware.params]
        [ring.util.response])
  (:require [szama.views :as views]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn layout [tpl] (views/layout tpl))

(defn users-index [req]
  (layout (views/users-index (select users))))

(defn users-create [req]
  (let [name ((req :params) :name)]
    (insert users (values {:name name}))
    (redirect "/users")))

(defn users-destroy [id]
  ; (str id))
  (delete users (where {:id (read-string id)} ))
  (redirect "/users"))


(defroutes app-routes
  (context "/users" [] (defroutes users-routes
    (context "/:id" [id] (defroutes user-routes
      (DELETE "/" [] (users-destroy id))))
    (GET  "/" [] users-index)
    (POST "/" [] users-create)))

  (GET "/" [] "Hello World")

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
    (wrap-params)
    ))
