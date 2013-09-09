(ns szama.db
  (:use
    [korma.db]
    [korma.core]
    [clj-bonecp-url.core])
  (:require [clojure.java.jdbc :as sql]))


(def datasource
  (datasource-from-url
    (or (System/getenv "DATABASE_URL")
        "postgres://teamon@localhost:5432/szama")))

(when (nil? @korma.db/_default)
  (korma.db/default-connection {:pool {:datasource datasource}}))


; for migrations
(defn invoke-with-connection [f]
  (sql/with-connection {:datasource datasource}
    (sql/transaction (f))))


(declare entries users orders)

(defentity entries
  (belongs-to orders)
  (belongs-to users))

(defentity users
  (has-many entries))

(defentity orders
  (has-many entries {:fk :order_id}))
