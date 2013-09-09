(ns migrations.20130909024815-create-orders
  (:use
    [korma.db]
    [korma.core]
    [szama.db])
  (:require [clojure.java.jdbc :as sql]))

(defn up
  "Migrates the database up to version 20130909024815."
  []
  (invoke-with-connection
    #(sql/create-table
      :orders
      [:id          "SERIAL" "PRIMARY KEY"]
      [:delivery    "INTEGER"]
      [:caller_id   "INTEGER"]
      [:created_at  "TIMESTAMP" "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn down
  "Migrates the database down from version 20130909024815."
  []
  (invoke-with-connection #(sql/drop-table :orders)))
