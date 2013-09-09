(ns migrations.20130909025144-create-entries
  (:use
   [korma.db]
   [korma.core]
   [szama.db])
  (:require [clojure.java.jdbc :as sql]))

(defn up
  "Migrates the database up to version 20130909025144."
  []
  (invoke-with-connection
    #(sql/create-table
      :entries
      [:id          "SERIAL" "PRIMARY KEY"]
      [:order_id    "INTEGER"]
      [:user_id     "INTEGER"]
      [:amount      "INTEGER"]
      [:created_at  "TIMESTAMP" "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn down
  "Migrates the database down from version 20130909025144."
  []
  (invoke-with-connection #(sql/drop-table :entries)))

