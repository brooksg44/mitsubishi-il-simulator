# Mitsubishi IL Simulator
## Simulator for the Mitsubishi IL Instruction Set.

## use the latest version of these libraries
# instaparse for parsing IL
# cljfx for the GUI

## use this EBNF for the IL instruction set
```enbf
(* Mitsubishi FX Series Instruction List (IL) - Simplified EBNF *)

program = { label ":" }, instruction, comment ;

label = identifier ;

instruction = 
    logical_instruction | 
    data_transfer_instruction |
    timer_instruction |
    counter_instruction | 
    control_flow_instruction | 
    special_instruction ;

logical_instruction = 
    "LD" modifier operand | 
    "LDI" modifier operand |
    "AND" modifier operand | 
    "ANI" modifier operand | 
    "OR" modifier operand | 
    "ORI" modifier operand |
    "XOR" modifier operand | 
    "XORI" modifier operand |
    "OUT" operand | 
    "OUTI" operand |
    "SET" operand | 
    "RST" operand |
    "PLS" operand |
    "PLF" operand ;

data_transfer_instruction =
    "MOV" operand "," operand |
    "DMOV" operand "," operand ;

timer_instruction =
    "T" timer_number "K" constant ;

counter_instruction =
    "C" counter_number "K" constant ;

control_flow_instruction =
    "JMP" label | 
    "JMPC" label |
    "CALL" label |
    "RET" ;

special_instruction =
    "ANB" | 
    "ORB" ;

modifier = [ "N" ] ; (* Negation modifier *)

operand = 
    device_address | 
    constant ;

device_address = 
    "X" octal_number | (* Input *)
    "Y" octal_number | (* Output *)
    "M" decimal_number | (* Internal Relay/Auxiliary Relay *)
    "L" decimal_number | (* Latching Relay *)
    "T" decimal_number | (* Timer Coil *)
    "C" decimal_number | (* Counter Coil *)
    "D" decimal_number ; (* Data Register *)


timer_number = decimal_number ;

counter_number = decimal_number ;

constant =
    decimal_number |
    "K" decimal_number ; (* Constant value *)

octal_number = digit { digit } ; (* 0-7 *)

decimal_number = digit { digit } ; (* 0-9 *)

identifier = letter { letter | digit | "_" } ;

comment = ";" { character } ;

digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;

letter = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" |
         "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z" ;

character = letter | digit | special_character ;

special_character = " " | "." | "," | ":" | "(" | ")" | "[" | "]" | "-" | "+" | "*" | "/" | "<" | ">" | "=" | "#" | "$" | "%" | "&" | "@" | "!" | "?" | "~" | "|" | "{" | "}" | "\\" | "^" | "`" | "_" ;
```
## Pay particular attention to the following instructions:
ANDB
ORB 
# See web site for more information on ANDB and ORB instructions.
## https://maintenanceworld.com/2014/12/08/plc-programming-list-mitsubishi-plc-part-2/#:~:text=Really%20Smart%20Mitsubishi%20FX%20PLC%20Rule%20%233

## https://docs.rs-online.com/ad97/0900766b80082ee7.pdf


# See Mitsubishi Official Manuals for more information on ANDB and ORB instructions.
## Such as: "MELSEC FX Family Programmable Logic Controllers Beginner's Manual"

# Make Inputs and Outputs have global scope
## Provide a way to toggle the inputs in the GUI
### Display true as green and false as black
## Display status of outputs in the GUI
### Display true as green and false as black

# Make the GUI more user friendly
## Add a button to clear the screen
## Add a button to save the program to a file
## Add a button to load the program from a file
## Add a button to run the program
## Add a button to stop the program
## Add a button to reset the program
## Add a button to exit the program
### Exit java on button click or window close