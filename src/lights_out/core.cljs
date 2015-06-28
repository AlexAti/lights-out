(ns ^:figwheel-always lights-out.core
    (:require
              [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:x 6 :y 4 :level 1 :activeno 0 :active [] :behaviour []}))

(defn active [x y]
  (get-in @app-state [:active y x]))

(defn toggle [x y]
  (swap! app-state update-in [:active y x] not))

(defn count-lit-tiles [grid]
  (apply + (map #(count (filter identity %))
                grid)))

(declare jump-level)
(defn update-score! []
  (let [grid (get @app-state :active)
        litno (count-lit-tiles grid)]
    (if (= 0 litno)
      (jump-level)
      (swap! app-state assoc :activeno litno))))

(defn singleton-behaviour [x y]
  (toggle x y)
  (update-score!))

(defn all-neighbors [i j x y]
  (doall (for [m [-1 0 1] :when (< -1 (+ m i) x)
               n [-1 0 1] :when (< -1 (+ n j) y)
                          :when (< 0 (+ (Math/abs m) (Math/abs n)))]
              [(+ m i) (+ n lj)])))

(defn cross-neighbors [i j x y]
  (doall (for [m [-1 0 1] :when (< -1 (+ m i) x)
               n [-1 0 1] :when (< -1 (+ n j) y)
                          :when (< 0 (+ (Math/abs m) (Math/abs n)) 2)]
              [(+ m i) (+ n j)])))

(defn classic-behaviour [i j]
  (let [{x :x y :y} @app-state
        neighbors (cross-neighbors i j x y)]
       (doseq [n neighbors]
         (apply toggle n))))

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
                    classic-behaviour
                    arandomone-behaviour
                    justmyrow-behaviour
                    justmycol-behaviour
                    wholecross-behaviour
                    alltherest-behaviour])

(defn behaviour [x y]
  (get-in @app-state [:behaviour y x]))

(defn behave [x y]
  ((behaviour x y) x y)
  (update-score!))

(let [numb (count behaviour-set)
      step (/ 360 numb)
      pos0 (int (rand 360))]
  (def behaviour-hue (zipmap behaviour-set
                             (map #(+ pos0 (* % step)) (range numb)))))

(defn hue [x y]
  (let [behaviour (behaviour x y)]
    (get behaviour-hue behaviour)))

(defn button-component [x y]
  [:a.button {:style {:color (if (active x y)
                                 (str "hsl(" (hue x y) ",80%,10%)")
                                 (str "hsl(" (hue x y) ",80%,50%)"))
                      :background-color (if (active x y)
                                            (str "hsl(" (hue x y) ",80%,50%)")
                                            (str "hsl(" (hue x y) ",80%,10%)"))}
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
  (let [{x :x y :y l :level} @app-state]
    (swap! app-state assoc :behaviour (random-matrix x y (subvec behaviour-set 0 l)))
    (swap! app-state assoc :active (random-matrix x y [true false]))
    (update-score!)))

(defn jump-level []
  (swap! app-state update :level inc)
  (restart-game))

(defn restart-button []
  [:a.restart
    {:on-click #(restart-game)}
    "Click here for a fresh (re)start!"])

(defn app-component []
  [:div.app
    [:div.header
      [:h1 "Click on buttons and turn them all off!"]
      [:h3 "You are now in level " (:level @app-state)". " (:activeno @app-state) " more tiles to go!"]
      [restart-button]
      [:p "(don't worry, you won't lose your level)"]]
    [matrix-component]])

(reagent/render-component [app-component]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
