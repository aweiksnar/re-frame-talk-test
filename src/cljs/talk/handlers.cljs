(ns talk.handlers
    (:require [re-frame.core :as re-frame]
              [talk.db :as db]
              [ajax.core :refer [GET]]))

(def talk-api "https://talk-staging.zooniverse.org")

(enable-console-print!)

(defn json->clj [s]
  "Convert json string to clojure data"
  (js->clj (.parse js/JSON s) :keywordize-keys true))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "Error" status " " status-text)))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-boards
 (fn
   [db [_ response]]
   (let [cljs-data (json->clj response)
         boards (get-in cljs-data [:boards])]
     (-> db
       (assoc :loading? false)
       (assoc :boards boards)))))

(re-frame/register-handler
 :get-boards
 (fn
   [db _]
   (GET (str talk-api "/boards")
        :format :json
        :response-format :raw
        :params {:section (:section db)}
        :handler #(re-frame/dispatch [:set-boards %])
        :error-handler error-handler)
   (assoc db :loading? true)))

(re-frame/register-handler
 :set-discussions
 (fn
   [db [_ response]]
   (let [cljs-data (json->clj response)
         discussions (get-in cljs-data [:discussions])]
     (-> db
       (assoc :loading? false)
       (assoc :discussions discussions)))))

(re-frame/register-handler
 :get-discussions
 (fn
   [db [_ {:keys [board]}]]
   (GET (str talk-api "/discussions")
        :format :json
        :response-format :raw
        :params {:section (:section db) :board_id (:id board)}
        :handler #(re-frame/dispatch [:set-discussions %])
        :error-handler error-handler)
   (assoc db :loading? true)))

(re-frame/register-handler
 :set-comments
 (fn
   [db [_ response]]
   (let [cljs-data (json->clj response)
         comments (get-in cljs-data [:comments])]
     (-> db
       (assoc :loading? false)
       (assoc :comments comments)))))

(re-frame/register-handler
 :get-comments
 (fn
   [db [_ {:keys [discussion]}]]
   (GET (str talk-api "/comments")
        :format :json
        :response-format :raw
        :params {:section (:section db) :discussion_id (:id discussion)}
        :handler #(re-frame/dispatch [:set-comments %])
        :error-handler error-handler)
   (assoc db :loading? true)))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))
