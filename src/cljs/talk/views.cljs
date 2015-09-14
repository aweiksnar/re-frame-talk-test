(ns talk.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [markdown.core :refer [md->html]]))

;; -------------------- Home
(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Welcome to " @name "!")
       :level :level1])))

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "About"
   :href "#/about"])


(defn list-of-boards []
  (let [boards (re-frame/subscribe [:boards])]
    (fn []
      [:div
       [re-com/title
        :label "Boards"
        :level :level2]
       (for [board @boards]
         ^{:key (:id board)}
         [re-com/hyperlink
          :on-click #(re-frame/dispatch [:get-discussions {:board board}])
          :label (str (:title board) " - " (:description board))])
       [re-com/button
        :label "Get boards"
        :on-click #(re-frame/dispatch [:get-boards])]])))

(defn list-of-discussions []
  (let [discussions (re-frame/subscribe [:discussions])]
    (fn []
      [:div
       (when-not (empty? @discussions)
         [re-com/title
          :label "Discussions"
          :level :level2])
       (for [discussion @discussions]
         ^{:key (:id discussion)}
         [re-com/hyperlink
          :on-click #(re-frame/dispatch [:get-comments {:discussion discussion}])
          :label (str (:title discussion))])])))

(defn list-of-comments []
  (let [comments (re-frame/subscribe [:comments])]
    (fn []
      [:div
       (when-not (empty? @comments)
         [re-com/title
          :label "Comments"
          :level :level2])

       (for [comment @comments]
         ^{:key (:id comment)}
         [:div.markdown {:dangerouslySetInnerHTML {:__html
                                          (md->html (:body comment))}}])])))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title]
              [re-com/h-box
               :children [[re-com/box
                           :child [list-of-boards]
                           :size "1"]
                          [re-com/box
                           :child [list-of-discussions]
                           :size "1"]
                          [re-com/box
                           :child [list-of-comments]
                           :size "1"]]]
              [link-to-about-page]]])

;; -------------------- About
(defn about-title []
  [re-com/title
   :label "About"
   :level :level1])

(defn about-description []
  [re-com/title
   :label "Talk is a place to chat about the zooniverse"
   :level :level2])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "Home "
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [about-description] [link-to-home-page]]])

;; -------------------- Panels
(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn top-nav []
  [:nav
   [link-to-home-page]
   [link-to-about-page]])

(defn loader []
  (let [loading (re-frame/subscribe [:loading?])]
    (when @loading [:i.zmdi.zmdi-flower-alt.zmdi-hc-2x
                    {:style {:position "absolute" :bottom "5px"}}])))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :padding "1vw"
       :children [[loader]
                  [top-nav]
                  (panels @active-panel)]])))
