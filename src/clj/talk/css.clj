(ns talk.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "navy"}]
  [:.level1 {:color "tomato"}]
  [:.markdown
   [:img {:max-width "100%"}]])

