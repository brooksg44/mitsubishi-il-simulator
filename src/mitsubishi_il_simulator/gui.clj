(ns mitsubishi-il-simulator.gui
  (:require [cljfx.api :as fx]
            [mitsubishi-il-simulator.simulator :as sim])
  (:import [javafx.application Platform]
           [javafx.scene.control Alert Alert$AlertType ButtonType]
           [javafx.stage FileChooser]
           [java.io File]))

;; GUI State
(def gui-state
  (atom {
    :program-text ""
    :output-log ""
    :running false
    :input-states (into {} (map #(vector % false) (range 8))) ; X0-X7
    :output-states (into {} (map #(vector % false) (range 8))) ; Y0-Y7
    }))

(defn update-gui-state! [key value]
  (swap! gui-state assoc key value))

(defn get-gui-state [key]
  (get @gui-state key))

;; File operations
(defn choose-file [title extension-filter save?]
  "Show file chooser dialog"
  (let [chooser (FileChooser.)]
    (.setTitle chooser title)
    (when extension-filter
      (.addAll (.getExtensionFilters chooser) 
               [(FileChooser$ExtensionFilter. (str extension-filter " files") 
                                            [(str "*." extension-filter)])]))
    (if save?
      (.showSaveDialog chooser nil)
      (.showOpenDialog chooser nil))))

(defn load-program-from-file []
  "Load program from file"
  (when-let [file (choose-file "Load IL Program" "il" false)]
    (try
      (let [content (slurp (.getAbsolutePath file))]
        (update-gui-state! :program-text content)
        (update-gui-state! :output-log 
                          (str (get-gui-state :output-log) 
                               "Loaded program from: " (.getName file) "\n")))
      (catch Exception e
        (update-gui-state! :output-log 
                          (str (get-gui-state :output-log) 
                               "Error loading file: " (.getMessage e) "\n"))))))

(defn save-program-to-file []
  "Save program to file"
  (when-let [file (choose-file "Save IL Program" "il" true)]
    (try
      (spit (.getAbsolutePath file) (get-gui-state :program-text))
      (update-gui-state! :output-log 
                        (str (get-gui-state :output-log) 
                             "Saved program to: " (.getName file) "\n"))
      (catch Exception e
        (update-gui-state! :output-log 
                          (str (get-gui-state :output-log) 
                               "Error saving file: " (.getMessage e) "\n"))))))"
;; GUI Event handlers
(defn on-input-toggle [address]
  "Handle input toggle"
  (sim/toggle-input address)
  (update-gui-state! :input-states 
                    (assoc (get-gui-state :input-states) 
                           address 
                           (sim/get-device-value :input address))))

(defn on-run-program []
  "Handle run program button"
  (let [program-text (get-gui-state :program-text)]
    (if (empty? (clojure.string/trim program-text))
      (update-gui-state! :output-log 
                        (str (get-gui-state :output-log) 
                             "Error: No program to run\n"))
      (let [load-result (sim/load-program program-text)]
        (if (:error load-result)
          (update-gui-state! :output-log 
                            (str (get-gui-state :output-log) 
                                 "Parse Error: " (:error load-result) "\n"))
          (do
            (sim/start-execution)
            (update-gui-state! :running true)
            (update-gui-state! :output-log 
                              (str (get-gui-state :output-log) 
                                   "Program started\n"))))))))

(defn on-stop-program []
  "Handle stop program button"
  (sim/stop-execution)
  (update-gui-state! :running false)
  (update-gui-state! :output-log 
                    (str (get-gui-state :output-log) 
                         "Program stopped\n")))

(defn on-reset-program []
  "Handle reset program button"  
  (sim/reset-plc-state!)
  (update-gui-state! :running false)
  (update-gui-state! :input-states (into {} (map #(vector % false) (range 8))))
  (update-gui-state! :output-states (into {} (map #(vector % false) (range 8))))
  (update-gui-state! :output-log 
                    (str (get-gui-state :output-log) 
                         "System reset\n")))

(defn on-clear-log []
  "Handle clear log button"
  (update-gui-state! :output-log ""))

(defn on-exit []
  "Handle exit button"
  (Platform/exit)
  (System/exit 0))

;; GUI Components
(defn input-button [address state]
  "Create an input toggle button"
  {:fx/type :button
   :text (str "X" address)
   :style (str "-fx-background-color: " 
               (if (get (:input-states state) address false) 
                 "green" 
                 "black") 
               "; -fx-text-fill: white;")
   :on-action (fn [_] (on-input-toggle address))
   :pref-width 60
   :pref-height 40})

(defn output-indicator [address state]
  "Create an output status indicator"
  {:fx/type :label
   :text (str "Y" address)
   :style (str "-fx-background-color: " 
               (if (get (:output-states state) address false) 
                 "green" 
                 "black") 
               "; -fx-text-fill: white; -fx-alignment: center;")
   :pref-width 60
   :pref-height 40
   :alignment :center})

(defn control-panel []
  "Create the control panel"
  {:fx/type :v-box
   :spacing 10
   :children [
     {:fx/type :label :text "Program Control" :style "-fx-font-weight: bold;"}
     {:fx/type :h-box
      :spacing 10
      :children [
        {:fx/type :button :text "Load" :on-action (fn [_] (load-program-from-file))}
        {:fx/type :button :text "Save" :on-action (fn [_] (save-program-to-file))}
        {:fx/type :button :text "Run" :on-action (fn [_] (on-run-program))}
        {:fx/type :button :text "Stop" :on-action (fn [_] (on-stop-program))}
        {:fx/type :button :text "Reset" :on-action (fn [_] (on-reset-program))}]}
     {:fx/type :h-box
      :spacing 10
      :children [
        {:fx/type :button :text "Clear Log" :on-action (fn [_] (on-clear-log))}
        {:fx/type :button :text "Exit" :on-action (fn [_] (on-exit))}]}]})

(defn input-panel [state]
  "Create the input panel"
  {:fx/type :v-box
   :spacing 10
   :children [
     {:fx/type :label :text "Inputs (X)" :style "-fx-font-weight: bold;"}
     {:fx/type :grid-pane
      :hgap 5
      :vgap 5
      :children (for [i (range 8)]
                  {:fx/type input-button
                   :address i
                   :state state
                   :grid-pane/column (mod i 4)
                   :grid-pane/row (quot i 4)})}]})

(defn output-panel [state]
  "Create the output panel"
  {:fx/type :v-box
   :spacing 10
   :children [
     {:fx/type :label :text "Outputs (Y)" :style "-fx-font-weight: bold;"}
     {:fx/type :grid-pane
      :hgap 5
      :vgap 5
      :children (for [i (range 8)]
                  {:fx/type output-indicator
                   :address i
                   :state state
                   :grid-pane/column (mod i 4)
                   :grid-pane/row (quot i 4)})}]})

(defn program-editor [state]
  "Create the program editor"
  {:fx/type :v-box
   :spacing 10
   :children [
     {:fx/type :label :text "IL Program Editor" :style "-fx-font-weight: bold;"}
     {:fx/type :text-area
      :text (:program-text state)
      :on-text-changed (fn [new-text] (update-gui-state! :program-text new-text))
      :pref-row-count 15
      :pref-column-count 40
      :style "-fx-font-family: monospace;"}]})

(defn output-log [state]
  "Create the output log"
  {:fx/type :v-box
   :spacing 10
   :children [
     {:fx/type :label :text "Output Log" :style "-fx-font-weight: bold;"}
     {:fx/type :text-area
      :text (:output-log state)
      :editable false
      :pref-row-count 10
      :pref-column-count 40
      :style "-fx-font-family: monospace; -fx-background-color: black; -fx-text-fill: green;"}]})

(defn main-window [state]
  "Create the main window"
  {:fx/type :stage
   :title "Mitsubishi IL Simulator"
   :width 1000
   :height 700
   :on-close-request (fn [_] (on-exit))
   :scene {:fx/type :scene
           :root {:fx/type :border-pane
                  :top (control-panel)
                  :left {:fx/type :v-box
                         :spacing 20
                         :padding 10
                         :children [(input-panel state) (output-panel state)]}
                  :center {:fx/type :v-box
                           :spacing 10
                           :padding 10
                           :children [(program-editor state)]}
                  :bottom {:fx/type :v-box
                           :padding 10
                           :children [(output-log state)]}}}}))

;; Renderer and App management
(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc main-window)))

(defn start-gui []
  "Start the GUI application"
  (fx/mount-renderer gui-state renderer))

(defn refresh-gui []
  "Refresh the GUI (call this periodically to update output states)"
  (update-gui-state! :output-states 
                    (into {} (map #(vector % (sim/get-device-value :output %)) 
                                 (range 8))))
  (swap! gui-state identity)) ; Trigger re-render

;; Auto-refresh timer (updates every 100ms when running)
(defn start-auto-refresh []
  "Start auto-refresh timer"
  (future
    (while true
      (Thread/sleep 100)
      (when (get-gui-state :running)
        (sim/step-execution)
        (refresh-gui)))))
