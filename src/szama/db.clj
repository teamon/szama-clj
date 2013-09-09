(ns szama.db
  (:use
    [korma.db]
    [korma.core])
  (:require [clojure.java.jdbc :as sql]))

(def dbspec {:classname   "org.postgresql.Driver"
             :subprotocol "postgresql"
             :subname "//localhost:5432/szama"
             :user     "teamon"
             :password ""})

; korma db
(defdb db dbspec)

; for migrations
(defn invoke-with-connection [f]
  (sql/with-connection
    dbspec
    (sql/transaction (f))))


(declare entries users orders)

(defentity entries
  (belongs-to orders)
  (belongs-to users))

(defentity users
  (has-many entries))

(defentity orders
  (has-many entries {:fk :order_id}))
