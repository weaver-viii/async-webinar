(ns webinar.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async
             :refer [>! <! put! take! chan timeout alts!]]
            [goog.events :as events])
  (:import [goog.events EventType]))

(enable-console-print!)

;; =============================================================================
;; Utilities

(defn by-id [id]
  (.getElementById js/document id))

(defn events->chan
  ([el event-type] (events->chan el event-type nil))
  ([el event-type xform]
   (let [c (chan 1 xform)]
     (events/listen el event-type
       (fn [e] (put! c e)))
     c)))

(defn mouse-loc->vec [e]
  [(.-clientX e) (.-clientY e)])

(defn show! [id s]
  (set! (.-innerHTML (.getElementById js/document id)) s))

;; =============================================================================
;; Example 1

(defn ex1 []
  (let [clicks (events->chan (by-id "ex0-button") EventType.CLICK)
        show! (partial show! "ex0-display")]
    (show! "Waiting for a click ...")
    (<! clicks)
    (show! "Got a click!")))

(ex1)
