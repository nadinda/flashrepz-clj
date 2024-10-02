(ns flashcard.frontend.app
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]
            ["react-dom/client" :as rdom]))

(defnc app []
  ((d/div
    (d/h1 "Halo Dunia"))))

(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))))