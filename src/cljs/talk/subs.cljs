(ns talk.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :name
 (fn [db]
   (reaction (:name @db))))

(re-frame/register-sub
 :boards
 (fn [db]
   (reaction (:boards @db))))

(re-frame/register-sub
 :discussions
 (fn [db]
   (reaction (:discussions @db))))

(re-frame/register-sub
 :comments
 (fn [db]
   (reaction (:comments @db))))

(re-frame/register-sub
 :loading?
 (fn [db]
   (reaction (:loading? @db))))

(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))
