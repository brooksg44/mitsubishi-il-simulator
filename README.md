# mitsubishi-il-simulator

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar mitsubishi-il-simulator-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2025 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
LD X0
OR X1      ; First branch

LD X2      ; Second branch starts
OR X3

ANB        ; AND the two branches
OUT Y0
```

### Set/Reset Logic
```il
; X0 sets memory M0, X1 resets M0
; M0 controls Y0
LD X0
SET M0

LD X1
RST M0

LD M0
OUT Y0
```

## EBNF Grammar

The simulator uses the following EBNF grammar for parsing IL programs:

```ebnf
program = line*
line = (label ":")? instruction comment?

instruction = 
    logical_instruction | 
    data_transfer_instruction |
    timer_instruction |
    counter_instruction | 
    control_flow_instruction | 
    special_instruction

logical_instruction = 
    "LD" modifier? operand | 
    "LDI" modifier? operand |
    "AND" modifier? operand | 
    "ANI" modifier? operand | 
    "OR" modifier? operand | 
    "ORI" modifier? operand |
    "XOR" modifier? operand | 
    "XORI" modifier? operand |
    "OUT" operand | 
    "OUTI" operand |
    "SET" operand | 
    "RST" operand |
    "PLS" operand |
    "PLF" operand

special_instruction = "ANB" | "ORB"

modifier = "N" (* Negation modifier *)
operand = device_address | constant
device_address = 
    "X" octal_number |     (* Input *)
    "Y" octal_number |     (* Output *)
    "M" decimal_number |   (* Internal Relay *)
    "L" decimal_number |   (* Latching Relay *)
    "T" decimal_number |   (* Timer Coil *)
    "C" decimal_number |   (* Counter Coil *)
    "D" decimal_number     (* Data Register *)
```

## Understanding ANB and ORB Instructions

The ANB (AND Block) and ORB (OR Block) instructions are special commands used to combine parallel logic branches:

### ANB (AND Block)
- Combines the current accumulator value with the previous branch using AND logic
- Used when you want both conditions to be true

### ORB (OR Block) 
- Combines the current accumulator value with the previous branch using OR logic
- Used when you want either condition to be true

### Example with ANB:
```il
; Branch 1: X0 OR X1
LD X0
OR X1

; Branch 2: X2 AND X3  
LD X2
AND X3

; Combine: (X0 OR X1) AND (X2 AND X3)
ANB
OUT Y0
```

### Example with ORB:
```il
; Branch 1: X0 AND X1
LD X0
AND X1

; Branch 2: X2 AND X3
LD X2  
AND X3

; Combine: (X0 AND X1) OR (X2 AND X3)
ORB
OUT Y0
```

## Architecture

The simulator consists of several key components:

- **Parser** (`parser.clj`): Uses Instaparse to parse IL programs according to the EBNF grammar
- **Simulator** (`simulator.clj`): Executes IL instructions and maintains PLC state
- **GUI** (`gui.clj`): JavaFX interface using cljfx for user interaction
- **Core** (`core.clj`): Main entry point that ties everything together

### Global State Management

The simulator maintains global state for:
- Input devices (X0-X7)
- Output devices (Y0-Y7)  
- Internal relays (M devices)
- Latching relays (L devices)
- Timer and counter devices
- Program execution state (program counter, accumulator, etc.)

## References

For more information about Mitsubishi PLC programming and IL instructions:

- [Mitsubishi FX Programming Manual](https://docs.rs-online.com/ad97/0900766b80082ee7.pdf)
- [PLC Programming List - Mitsubishi PLC Part 2](https://maintenanceworld.com/2014/12/08/plc-programming-list-mitsubishi-plc-part-2/)
- MELSEC FX Family Programmable Logic Controllers Beginner's Manual

## Development

### Project Structure
```
mitsubishi-il-simulator/
├── src/mitsubishi_il_simulator/
│   ├── core.clj          # Main entry point
│   ├── parser.clj        # IL grammar and parsing
│   ├── simulator.clj     # Execution engine  
│   └── gui.clj          # JavaFX GUI
├── resources/examples/   # Example IL programs
├── project.clj          # Leiningen project file
└── README.md           # This file
```

### Dependencies
- **Clojure 1.11.1**: Core language
- **Instaparse 1.4.12**: Grammar-based parsing
- **cljfx 1.7.24**: JavaFX wrapper for Clojure
- **OpenJFX 17.0.2**: JavaFX runtime

### Building
```bash
# Run tests
lein test

# Create uberjar
lein uberjar

# Run from jar
java -jar target/uberjar/mitsubishi-il-simulator-0.1.0-SNAPSHOT-standalone.jar
```

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## License

Copyright © 2024 Gregory Brooks

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
