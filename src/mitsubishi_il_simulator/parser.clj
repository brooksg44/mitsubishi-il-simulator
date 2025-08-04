(ns mitsubishi-il-simulator.parser
  (:require [instaparse.core :as insta]))

(def il-grammar
  "
  (* Mitsubishi FX Series Instruction List (IL) - Simplified EBNF *)
  
  program = (line <eol>?)*
  line = instruction_line | comment_line | empty_line
  instruction_line = <ws>? (label <':'> <ws>?)? instruction (<ws> comment)?
  comment_line = <ws>? comment  
  empty_line = <ws>?
  
  eol = '\n' | '\r\n' | '\r'
  
  label = identifier
  
  instruction = 
      logical_instruction | 
      data_transfer_instruction |
      timer_instruction |
      counter_instruction | 
      control_flow_instruction | 
      special_instruction
  
  logical_instruction = 
      'LD' <ws>? modifier? <ws>? operand | 
      'LDI' <ws>? modifier? <ws>? operand |
      'AND' <ws>? modifier? <ws>? operand | 
      'ANI' <ws>? modifier? <ws>? operand | 
      'OR' <ws>? modifier? <ws>? operand | 
      'ORI' <ws>? modifier? <ws>? operand |
      'XOR' <ws>? modifier? <ws>? operand | 
      'XORI' <ws>? modifier? <ws>? operand |
      'OUT' <ws>? operand | 
      'OUTI' <ws>? operand |
      'SET' <ws>? operand | 
      'RST' <ws>? operand |
      'PLS' <ws>? operand |
      'PLF' <ws>? operand
  
  data_transfer_instruction =
      <'MOV'> <ws> operand <ws> <','> <ws> operand |
      <'DMOV'> <ws> operand <ws> <','> <ws> operand
  
  timer_instruction =
      <'T'> timer_number <ws> <'K'> constant
  
  counter_instruction =
      <'C'> counter_number <ws> <'K'> constant
  
  control_flow_instruction =
      <'JMP'> <ws> label | 
      <'JMPC'> <ws> label |
      <'CALL'> <ws> label |
      <'RET'>
  
  special_instruction =
      'ANB' | 
      'ORB'
  
  modifier = 'N' (* Negation modifier *)
  
  operand = 
      device_address | 
      constant
  
  device_address = 
      input_address | output_address | memory_address | latch_address |
      timer_address | counter_address | data_address
      
  input_address = <'X'> octal_number (* Input *)
  output_address = <'Y'> octal_number (* Output *)
  memory_address = <'M'> decimal_number (* Internal Relay/Auxiliary Relay *)
  latch_address = <'L'> decimal_number (* Latching Relay *)
  timer_address = <'T'> decimal_number (* Timer Coil *)
  counter_address = <'C'> decimal_number (* Counter Coil *)
  data_address = <'D'> decimal_number (* Data Register *)
  
  timer_number = decimal_number
  
  counter_number = decimal_number
  
  constant =
      decimal_number |
      <'K'> decimal_number (* Constant value *)
  
  octal_number = #'[0-7]+'
  
  decimal_number = #'[0-9]+'
  
  identifier = #'[A-Za-z][A-Za-z0-9_]*'
  
  comment = <';'> #'[^\r\n]*'
  
  ws = #'[ \t]+'
  ")

(def il-parser (insta/parser il-grammar))

(defn parse-il [program-text]
  "Parse IL program text and return the parse tree"
  (let [result (il-parser program-text)]
    (if (insta/failure? result)
      {:error (insta/get-failure result)}
      {:success result})))

(defn instruction-type [parsed-instruction]
  "Extract the instruction type from parsed instruction"
  (when (vector? parsed-instruction)
    (first parsed-instruction)))
