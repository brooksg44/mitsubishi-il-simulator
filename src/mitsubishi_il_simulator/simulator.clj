(ns mitsubishi-il-simulator.simulator
  (:require [mitsubishi-il-simulator.parser :as parser]))

;; Global state for the PLC simulator
(def plc-state 
  (atom {
    ;; Device memory
    :inputs {}      ; X devices (inputs) - octal addressing
    :outputs {}     ; Y devices (outputs) - octal addressing  
    :memory {}      ; M devices (internal relays)
    :latches {}     ; L devices (latching relays)
    :timers {}      ; T devices (timer coils and values)
    :counters {}    ; C devices (counter coils and values)
    :data {}        ; D devices (data registers)
    
    ;; Program execution state
    :program []     ; Parsed program instructions
    :pc 0           ; Program counter
    :running false  ; Execution state
    :accumulator false ; Current logic state
    :stack []       ; Stack for ANB/ORB operations
    :labels {}      ; Label to instruction index mapping
    
    ;; Timers and counters state
    :timer-values {}
    :counter-values {}
    }))

(defn reset-plc-state! []
  "Reset the PLC state to initial values"
  (reset! plc-state {
    :inputs {}
    :outputs {}
    :memory {}
    :latches {}
    :timers {}
    :counters {}
    :data {}
    :program []
    :pc 0
    :running false
    :accumulator false
    :stack []
    :labels {}
    :timer-values {}
    :counter-values {}
    }))

(defn get-device-value [device-type address]
  "Get the value of a device"  (let [state @plc-state
        device-map (case device-type
                     :input (:inputs state)
                     :output (:outputs state)
                     :memory (:memory state)
                     :latch (:latches state)
                     :timer (:timers state)
                     :counter (:counters state)
                     :data (:data state)
                     {})]
    (get device-map address false)))

(defn set-device-value! [device-type address value]
  "Set the value of a device"
  (swap! plc-state update device-type assoc address value))

(defn parse-operand [operand]
  "Parse an operand and return [device-type address]"
  (when (vector? operand)
    (let [op-type (first operand)
          addr (second operand)]
      (case op-type
        :input_address [:input (Integer/parseInt addr 8)] ; Octal for inputs
        :output_address [:output (Integer/parseInt addr 8)] ; Octal for outputs
        :memory_address [:memory (Integer/parseInt addr)]
        :latch_address [:latch (Integer/parseInt addr)]
        :timer_address [:timer (Integer/parseInt addr)]
        :counter_address [:counter (Integer/parseInt addr)]
        :data_address [:data (Integer/parseInt addr)]
        :constant [nil (Integer/parseInt addr)]
        :decimal_number [nil (Integer/parseInt addr)]
        nil))))

(defn execute-logical-instruction [instruction]
  "Execute a logical instruction"
  (let [instr-parts (rest instruction) ; Skip :logical_instruction tag
        instr-type (first instr-parts)
        operand (last instr-parts)
        has-modifier (> (count instr-parts) 2)
        modifier (when has-modifier (nth instr-parts 1))
        [device-type address] (parse-operand operand)
        device-value (if device-type
                      (get-device-value device-type address)
                      address) ; It's a constant
        negated (and has-modifier (= modifier "N"))
        final-value (if negated (not device-value) device-value)]
    
    (case instr-type
      "LD" (do
             ; Push current accumulator to stack when starting new branch
             (when (and (:accumulator @plc-state) (seq (:stack @plc-state)))
               (swap! plc-state update :stack conj (:accumulator @plc-state)))
             (swap! plc-state assoc :accumulator final-value))
      "LDI" (do
              (when (and (:accumulator @plc-state) (seq (:stack @plc-state)))
                (swap! plc-state update :stack conj (:accumulator @plc-state)))
              (swap! plc-state assoc :accumulator (not final-value)))
      "AND" (swap! plc-state update :accumulator #(and % final-value))
      "ANI" (swap! plc-state update :accumulator #(and % (not final-value)))
      "OR" (swap! plc-state update :accumulator #(or % final-value))
      "ORI" (swap! plc-state update :accumulator #(or % (not final-value)))
      "XOR" (swap! plc-state update :accumulator #(not= % final-value))
      "XORI" (swap! plc-state update :accumulator #(not= % (not final-value)))
      "OUT" (when device-type
              (set-device-value! device-type address (:accumulator @plc-state)))
      "OUTI" (when device-type
               (set-device-value! device-type address (not (:accumulator @plc-state))))
      "SET" (when device-type
              (set-device-value! device-type address true))
      "RST" (when device-type
              (set-device-value! device-type address false)))))))

(defn execute-special-instruction [instruction]
  "Execute special instructions like ANB and ORB"
  (let [instr-parts (rest instruction) ; Skip :special_instruction tag
        instr-type (first instr-parts)]
    (case instr-type
      "ANB" (let [stack (:stack @plc-state)
                  acc (:accumulator @plc-state)]
              (when (seq stack)
                (let [top-value (first stack)
                      new-acc (and acc top-value)]
                  (swap! plc-state assoc :accumulator new-acc)
                  (swap! plc-state update :stack rest))))
      "ORB" (let [stack (:stack @plc-state)
                  acc (:accumulator @plc-state)]
              (when (seq stack)
                (let [top-value (first stack)
                      new-acc (or acc top-value)]
                  (swap! plc-state assoc :accumulator new-acc)
                  (swap! plc-state update :stack rest)))))))

(defn execute-instruction [instruction]
  "Execute a single IL instruction"
  (when (vector? instruction)
    (let [instr-type (first instruction)]
      (case instr-type
        :logical_instruction (execute-logical-instruction instruction)
        :special_instruction (execute-special-instruction instruction)
        :line (when (> (count instruction) 1)
                (execute-instruction (second instruction))) ; Handle line wrapper
        ;; Add other instruction types as needed
        nil))))

(defn step-execution []
  "Execute one instruction step"
  (let [state @plc-state
        pc (:pc state)
        program (:program state)]
    (when (and (:running state) (< pc (count program)))
      (let [instruction (nth program pc)]
        (execute-instruction instruction)
        (swap! plc-state update :pc inc)))))

(defn load-program [program-text]
  "Load and parse a program"
  (let [parse-result (parser/parse-il program-text)]
    (if (:error parse-result)
      {:error (:error parse-result)}
      (do
        ;; Extract instructions from parsed program
        (let [parsed-program (:success parse-result)
              instructions (if (= (first parsed-program) :program)
                            (rest parsed-program) ; Skip :program tag
                            [parsed-program])]
          (swap! plc-state assoc :program instructions)
          (swap! plc-state assoc :pc 0)
          {:success "Program loaded successfully"})))))

(defn start-execution []
  "Start program execution"
  (swap! plc-state assoc :running true))

(defn stop-execution []
  "Stop program execution"
  (swap! plc-state assoc :running false))

(defn get-plc-state []
  "Get current PLC state"
  @plc-state)

;; Input/Output helper functions for GUI
(defn toggle-input [address]
  "Toggle an input device"
  (let [current-value (get-device-value :input address)]
    (set-device-value! :input address (not current-value))))

(defn set-input [address value]
  "Set an input device value"
  (set-device-value! :input address value))

(defn get-output [address]
  "Get an output device value"
  (get-device-value :output address))
