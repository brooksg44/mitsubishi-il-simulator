# Mitsubishi IL Simulator

A simulator for the Mitsubishi FX Series Instruction List (IL) programming language, built with Clojure and JavaFX.

## Features

- **IL Parser**: Complete parser for Mitsubishi IL instruction set using Instaparse
- **Real-time Simulation**: Execute IL programs step by step
- **Interactive GUI**: User-friendly interface with JavaFX/cljfx
- **I/O Control**: Toggle inputs (X0-X7) and monitor outputs (Y0-Y7)
- **Visual Feedback**: Green = ON/True, Black = OFF/False
- **Program Management**: Load, save, edit, and run IL programs
- **ANB/ORB Support**: Advanced block operations for complex logic

## Supported Instructions

### Logical Instructions
- **LD/LDI**: Load/Load Inverted
- **AND/ANI**: AND/AND Inverted  
- **OR/ORI**: OR/OR Inverted
- **XOR/XORI**: XOR/XOR Inverted
- **OUT/OUTI**: Output/Output Inverted
- **SET/RST**: Set/Reset
- **PLS/PLF**: Pulse/Pulse Falling

### Special Instructions
- **ANB**: AND Block (combines parallel branches with AND)
- **ORB**: OR Block (combines parallel branches with OR)

### Device Types
- **X**: Input devices (octal addressing X0-X7)
- **Y**: Output devices (octal addressing Y0-Y7)
- **M**: Internal relays/auxiliary relays
- **L**: Latching relays
- **T**: Timer coils
- **C**: Counter coils
- **D**: Data registers

## Installation and Usage

### Prerequisites
- Java 11 or higher with JavaFX modules
- Leiningen (Clojure build tool)

### Running the Simulator

1. Clone the repository:
```bash
git clone https://github.com/gregorybrooks/mitsubishi-il-simulator.git
cd mitsubishi-il-simulator
```

2. Install dependencies:
```bash
lein deps
```

3. Run the simulator:
```bash
lein run
```

Or use the provided startup scripts:
```bash
./run.sh        # Unix/Mac
run.bat         # Windows
```

### Using the GUI

1. **Program Editor**: Write or paste IL programs in the center text area
2. **Input Controls**: Click X0-X7 buttons to toggle inputs (Green=ON, Black=OFF)
3. **Output Indicators**: Monitor Y0-Y7 outputs (Green=ON, Black=OFF)
4. **Control Buttons**:
   - **Load**: Load program from .il file
   - **Save**: Save current program to .il file
   - **Run**: Start program execution
   - **Stop**: Stop program execution
   - **Reset**: Reset all devices and stop program
   - **Clear Log**: Clear the output log
   - **Exit**: Close the simulator

## Example Programs

### Simple Logic
```il
; Load input X0 and output to Y0
LD X0
OUT Y0

; Load input X1 inverted and output to Y1
LD X1
OUTI Y1
```

### AND Logic
```il
; X0 AND X1 controls Y0
LD X0
AND X1
OUT Y0
```

### OR Logic with ANB/ORB
```il
; (X0 OR X1) AND (X2 OR X3) controls Y0
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

## Dependencies

- **Clojure 1.12.0**: Core language
- **Instaparse 1.4.12**: Grammar-based parsing
- **cljfx 1.7.24**: JavaFX wrapper for Clojure
- **OpenJFX 17.0.2**: JavaFX runtime

## Building

```bash
# Run tests
lein test

# Create uberjar
lein uberjar

# Run from jar
java -jar target/uberjar/mitsubishi-il-simulator-0.1.0-SNAPSHOT-standalone.jar
```

## References

For more information about Mitsubishi PLC programming and IL instructions:

- [Mitsubishi FX Programming Manual](https://docs.rs-online.com/ad97/0900766b80082ee7.pdf)
- [PLC Programming List - Mitsubishi PLC Part 2](https://maintenanceworld.com/2014/12/08/plc-programming-list-mitsubishi-plc-part-2/)
- MELSEC FX Family Programmable Logic Controllers Beginner's Manual

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## License

Copyright © 2024 Gregory Brooks

Distributed under the Eclipse Public License either version 2.0 or (at your option) any later version.
