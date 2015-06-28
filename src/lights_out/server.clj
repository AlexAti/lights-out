(ns lights-out.server
  (:require
    [org.httpkit.server :as http :refer [run-server]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [compojure.core :refer [defroutes]]
    [compojure.route :refer [files not-found]]
    [hiccup.core :refer :all]))


(defn not-found-page []
  (html
    [:div.notfoundmessage
      [:p "I'm afraid there's anything here. "
        [:a {:href "/index.html"} "Try going to the landing page!"]]]))

(defroutes all-routes
  (files "/resources/public/")
  (not-found (not-found-page)))

(defn -main [& args]
  (let [port (Integer/parseInt
               (or (System/getenv "PORT")
                   (first args)
                   "8087"))]
    (run-server (wrap-defaults all-routes site-defaults) {:port port})))
