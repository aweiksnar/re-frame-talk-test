(ns talk.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [talk.handlers]
              [talk.subs]
              [talk.routes :as routes]
              [talk.views :as views]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
