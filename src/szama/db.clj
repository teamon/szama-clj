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
     (sql/transaction (f))
    )
  )

(defentity users)
