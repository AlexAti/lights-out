(ns ^:figwheel-always lights-out.core
    (:require
              [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:x 6 :y 4 :active [[true true true true true true]
                                             [true true true true true true]
                                             [true true false true true true]
                                             [true true true true true true]]}))

(defn active [x y]
  (get-in @app-state [:active y x]))

(defn toggle [x y]
  (swap! app-state update-in [:active y x] not))

(defn button-component [x y]
  [:a {:class (str "button " (when (active x y) "lit"))
       :on-click #(toggle x y)}])

(defn matrix-component []
  [:div#matrix
    (for [col (range (:x @app-state))]
      [:div.col
        (for [row (range (:y @app-state))]
          [button-component col row])])])

(reagent/render-component [matrix-component]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
