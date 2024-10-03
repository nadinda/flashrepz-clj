(ns flashcard.frontend.app
  (:require [helix.core :refer [defnc $]]
            [helix.hooks :as hooks]
            [helix.dom :as d]
            ["react-dom/client" :as rdom]))

(def questions (atom [{:question ".at is the top-level domain for what country?"
                       :options ["Argentina" "Austria" "Australia"]
                       :answer "Austria"}
                      {:question "What is the capital city of Australia?"
                       :options ["Sydney" "Melbourne" "Canberra" "Perth"]
                       :answer "Canberra"}
                      {:question "Which planet is known as the Red Planet?"
                       :options ["Earth" "Mars" "Venus" "Jupiter"]
                       :answer "Mars"}]))

(defnc card-item [{:keys [question options answer flipped set-flipped]}]
  (d/div {:style {:background-color "#fffbea"
                  :width "400px"
                  :padding "20px"
                  :text-align "center"
                  :cursor "pointer"
                  :font-family "Poppins, sans-serif"}
          :on-click (flip-card set-flipped)}
         (if flipped
           ;; Show the answer side
           (d/div
            (d/p {:style {:color "black" :font-weight "bold"}} "Answer:")
            (d/p {:style {:color "green"}} answer))
           ;; Show the question side with options
           (d/div
            (d/p {:style {:font-weight "bold", :color "black"}} question)
            (d/div
             (for [option options]
               ^{:key option}
               (d/div {:style {:color "black"}} option)))))))

(defnc card-display []
  (let [[state set-state] (hooks/use-state {:current-question-index 0
                                            :flipped false})
        current-question (get @questions (:current-question-index state))
        flipped (:flipped state)]


    (defn flip-card []
      (fn []
        (set-state update :flipped not)))

    (d/div
     (d/h1 {:style {:text-align "center"}} "Flashrepz 🔥")
     (d/p {:style {:text-align "center"}} "(click to flip)")
     (d/div {:style {:display "flex"
                     :align-items "center"
                     :justify-content "center"
                     :color "yellow"}}
            ($ card-item {:question (:question current-question)
                          :options (:options current-question)
                          :answer (:answer current-question)
                          :flipped flipped
                          :set-flipped flip-card})))))

(defnc app []
  ($ card-display))


(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))))