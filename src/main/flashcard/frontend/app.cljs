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
  (d/div {:style {:background-color "#ffecba"
                  :padding "20px"
                  :border-radius "10px"
                  :box-shadow "0 12px 12px rgba(0,0,0,0.1)"
                  :width "400px"
                  :text-align "center"
                  :cursor "pointer"}
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

(defn card-navigation [direction set-state]
  (fn []
    (set-state (fn [state]
                 (let [total-questions (count @questions)
                       current-index (:current-question-index state)
                       new-index (mod (+ current-index direction) total-questions)]
                   (assoc state
                          :current-question-index new-index
                          :flipped false))))))

(defnc card-display []
  (let [[state set-state] (hooks/use-state {:current-question-index 0
                                            :flipped false})
        current-question (get @questions (:current-question-index state))
        flipped (:flipped state)]

    (defn flip-card []
      (fn []
        (set-state update :flipped not)))

    (d/div {:style {:display "flex"
                    :align-items "center"
                    :justify-content "center"
                    :flex-direction "column"
                    :font-family "Poppins, sans-serif"}}
           (d/h1 {:style {:text-align "center"}} "Flashrepz 🔥")
           (d/p {:style {:text-align "center"}} "(click card to flip)")
           (d/div {:style {:color "yellow"}}
                  ($ card-item {:question (:question current-question)
                                :options (:options current-question)
                                :answer (:answer current-question)
                                :flipped flipped
                                :set-flipped flip-card}))
           (d/div {:style {:margin-top "10px"}}
                  (d/button {:on-click (card-navigation 1 set-state)
                             :style {:background-color "#edad00"
                                     :color "black"
                                     :border "none"
                                     :padding "10px"
                                     :cursor "pointer"}} "Next")))))

(defnc app []
  ($ card-display))

(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))))