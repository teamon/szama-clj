(ns szama.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn users-index [x] "Hello")

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/users" [] users-index)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
