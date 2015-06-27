(ns ^:figwheel-always lights-out.core
    (:require
              [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:x 6 :y 4 :behaviour [] :active []}))

(defn active [x y]
  (get-in @app-state [:active y x]))

(defn toggle [x y]
  (swap! app-state update-in [:active y x] not))

(defn singleton-behaviour [x y]
  (toggle x y))

(defn arandomone-behaviour [x y]
  (let [{x :x y :y} @app-state
        i (int (rand x))
        j (int (rand y))]
    (toggle i j)))

(defn justmyrow-behaviour [x y]
  (dotimes [i (get @app-state :x)]
    (toggle i y)))

(defn justmycol-behaviour [x y]
  (dotimes [j (get @app-state :y)]
    (toggle x j)))

(defn wholecross-behaviour [x y]
  (justmycol-behaviour x y)
  (justmyrow-behaviour x y))

(defn alltherest-behaviour [x y]
  (toggle x y)
  (let [{x :x y :y} @app-state]
    (dotimes [i x]
      (dotimes [j y]
        (toggle i j)))))

(def behaviour-set [singleton-behaviour
                    arandomone-behaviour
                    justmyrow-behaviour
                    justmycol-behaviour
                    alltherest-behaviour
                    alltherest-behaviour])

(defn behave [x y]
  ((get-in @app-state [:behaviour y x]) x y))

(defn button-component [x y]
  [:a {:class (str "button " (when (active x y) "lit"))
       :on-click #(behave x y)}
       (if (active x y) "ON" "OFF")])

(defn matrix-component []
  [:div#matrix
    (for [col (range (:x @app-state))]
      [:div.col
        (for [row (range (:y @app-state))]
          [button-component col row])])])

(defn random-matrix [x y choices]
  (vec (for [j (range y)]
    (vec (for [i (range x)]
      (rand-nth choices))))))

(defn restart-game []
  (let [{x :x y :y} @app-state]
    (swap! app-state assoc :active (random-matrix x y [true false]))
    (swap! app-state assoc :behaviour (random-matrix x y behaviour-set))))

(defn restart-button []
  [:a.restart
    {:on-click #(restart-game)}
    "Click here for a new game!"])

(defn app-component []
  [:div.app
    [:div.header
      [:h1 "Click on buttons and turn them all off!"]
      [restart-button]]
    [matrix-component]])

(reagent/render-component [app-component]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
