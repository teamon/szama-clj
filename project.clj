(defproject szama "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [ [org.clojure/clojure "1.5.1"]
                  [compojure "1.1.5"]
                  [korma "0.3.0-RC5"]
                  [postgresql "9.1-901.jdbc4"]
                  [hiccup "1.0.4"]
                  [clj-bonecp-url "0.1.0"]
                  [ring/ring-jetty-adapter "1.1.6"]]
  :plugins [[lein-ring "0.8.5"]
            [drift "1.5.2"]]
  :min-lein-version "2.0.0"
  :ring {:handler szama.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
