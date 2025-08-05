;; This is not valid Clojure code, but rather the expected output of a test run.
;; Gave it the clj extension to get syntax highlighting and rainbow highlighting.

;;Testing complex logic parsing:
;;Parsing
;;LD X0
;;AND X1
;;ORB
;;OUT Y0
;;result:
{:success [:program
           [:line
            [:instruction_line
             [:instruction
              [:logical_instruction
               "LD"
               [:operand
                [:device_address [:input_address [:octal_number "0"]]]]]]]]
           [:line [:empty_line]]
           [:line
            [:instruction_line
             [:instruction
              [:logical_instruction
               "AND"
               [:operand
                [:device_address [:input_address [:octal_number "1"]]]]]]]]
           [:line [:empty_line]]
           [:line
            [:instruction_line [:instruction [:special_instruction "ORB"]]]]
           [:line [:empty_line]]
           [:line
            [:instruction_line
             [:instruction
              [:logical_instruction
               "OUT"
               [:operand
                [:device_address [:output_address [:octal_number "0"]]]]]]]]]}