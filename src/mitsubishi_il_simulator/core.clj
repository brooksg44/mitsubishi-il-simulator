(ns mitsubishi-il-simulator.core
  (:require [mitsubishi-il-simulator.gui :as gui]
            [mitsubishi-il-simulator.simulator :as sim])
  (:gen-class))

(defn -main
  "Main entry point for the Mitsubishi IL Simulator"
  [& args]
  (println "Starting Mitsubishi IL Simulator...")

  ;; Initialize the simulator
  (sim/reset-plc-state!)

  ;; Start the GUI
  (gui/start-gui)

  ;; Start the auto-refresh timer
  (gui/start-auto-refresh)

  (println "Simulator started successfully!")
  (println "Use the GUI to load, edit, and run IL programs.")
  (println "Toggle inputs with the X buttons and observe outputs on the Y indicators.")
  (println "Green = True/ON, Black = False/OFF")
  (println "Close the window or press Ctrl+C to exit.")

  ;; Keep the main thread alive to prevent application exit
  (let [shutdown-hook (Thread. #(println "Shutting down simulator..."))]
    (.addShutdownHook (Runtime/getRuntime) shutdown-hook)
    ;; Block main thread until interrupted
    (try
      (Thread/sleep Long/MAX_VALUE)
      (catch InterruptedException _
        (println "Application interrupted")))))

(comment
  ;; Flowstorm debugging entry point
  :dbg

  (-main))
