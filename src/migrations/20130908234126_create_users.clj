(ns migrations.20130908234126-create-users
  (:use
   [korma.db]
   [korma.core]
   [szama.db]
   )
  (:require [clojure.java.jdbc :as sql]))

(defn up
  "Migrates the database up to version 20130908234126."
  []
  (invoke-with-connection
    #(sql/create-table
      :users
      [:id          "SERIAL" "PRIMARY KEY"]
      [:name        "VARCHAR(255)"]
      [:created_at  "TIMESTAMP" "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))


(defn down
  "Migrates the database down from version 20130908234126."
  []
  (invoke-with-connection
    #(sql/drop-table :users)))
