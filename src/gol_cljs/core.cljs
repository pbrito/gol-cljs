(ns gol-cljs.core (:require-macros [cljs.core.async.macros :refer (go-loop)])
  (:require [reagent.core :as reagent]
            [cljs.core.async :refer (chan timeout put! <!)]))

(enable-console-print!)


(println "This text is printed from src/gol-cljs/core.cljs. Go ahead and edit it and see reloading in action.")

(def EVENTCHANNEL (chan))
;; define your app data so that it doesn't get over-written on reload

;(defonce app-state (atom {:text "Hello wddorld!"}))

(defonce state (reagent/atom  #{[26, 3], [24, 4], [26, 4], [14, 5], [15, 5], [22, 5], [23, 5], [36, 5], [37, 5] , [13, 6], [17, 6], [22, 6], [23, 6], [36, 6], [37, 6], [2, 7], [3, 7], [12, 7] , [18, 7], [22, 7], [23, 7], [2, 8], [3, 8], [12, 8], [16, 8], [18, 8], [19, 8] , [24, 8], [26, 8], [12, 9], [18, 9], [26, 9], [13, 10], [17, 10], [14, 11], [15, 11]} ))

(defn neighbors
  [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not= [dx dy ][0 0])]
     [(+ x dx) (+ y dy)]))

(defn next-pop [pop]
  (let [all-neighbors (mapcat neighbors pop)
          neigh-count (frequencies all-neighbors)]
      (set (for [[cell count] neigh-count
                 :when (or (= 3 count)
                           (and (= 2 count)
                                (and (< (get cell 0) 38)
                                  (pop cell))))]
             cell))))

(defn grid [] [:div {
                     :style {
                             :background-color "#a024"}}
                 (for [[x y] @state]
                   (if  (and (> x -100)(> y -100)(< x 200)(< y 200))
                    (let [ji (str (* x 2) "x" (* y 2))]
                      [:div {
                              :key ji
                              :style{:background-color "blue";
                                      :position "absolute"
                                      :height 9
                                      :width  9
                                      :left (+ (* x 9)  150)
                                      :top (+(* y 9) 200)}}])))])




(reagent/render [grid] (js/document.querySelector "#app"))

(go-loop []
    (<! (timeout 1))
    (swap! state next-pop)
    ;(js/console.log "event-data")
    (recur))

(go-loop []
    (<! (timeout 10))
    (swap! state next-pop)
    ;(js/console.log "event-data")
    (recur))

(go-loop []
    (<! (timeout 2))
    (swap! state next-pop)
    ;(js/console.log "event-data")
    (recur))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ; (swap! app-state update-in [:__figwheel_counter] inc)
)
