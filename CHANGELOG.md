# Changelog

All notable changes to the Mitsubishi IL Simulator project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2024-08-03

### Added
- **Core Parser Implementation**
  - Complete EBNF grammar for Mitsubishi FX Series IL instruction set
  - Instaparse-based parser with error handling
  - Support for all basic logical instructions (LD, AND, OR, XOR, OUT, SET, RST)
  - Device addressing support (X, Y, M, L, T, C, D devices)
  - Comment and whitespace handling

- **Simulation Engine**
  - PLC state management with global device memory
  - Instruction execution engine with accumulator logic
  - Support for special block instructions (ANB, ORB)
  - Device value get/set operations with type safety
  - Program loading and execution control (start/stop/reset)

- **JavaFX GUI Interface**
  - Real-time input/output monitoring and control
  - Visual indicators (Green=ON/True, Black=OFF/False)
  - Interactive input toggles (X0-X7)
  - Output status display (Y0-Y7)
  - Program editor with syntax highlighting preparation
  - Control panel with load/save/run/stop/reset functionality
  - Output log for system messages and debugging

- **Program Management**
  - Load IL programs from .il files
  - Save programs to files
  - Multiple example programs included
  - Error handling and user feedback

- **Example Programs**
  - `simple_logic.il` - Basic input/output operations
  - `complex_logic.il` - ANB/ORB block operations
  - `set_reset.il` - Memory and latch operations
  - `traffic_light.il` - State machine example
  - `motor_control.il` - Industrial control example

- **Development Tools**
  - Comprehensive unit test suite
  - Cross-platform startup scripts (Unix/Windows)
  - Development documentation and architecture guide
  - Leiningen project configuration with latest dependencies

- **Documentation**
  - Complete README with usage instructions
  - Development guide with extension examples
  - EBNF grammar documentation
  - ANB/ORB instruction explanations with examples

### Technical Details
- **Dependencies**
  - Clojure 1.11.1
  - Instaparse 1.4.12 for parsing
  - cljfx 1.7.24 for GUI (JavaFX wrapper)
  - OpenJFX 17.0.2 modules

- **Architecture**
  - Modular design with clear separation of concerns
  - Functional programming principles throughout
  - Immutable data structures with controlled state mutation
  - Event-driven GUI updates with automatic refresh

- **Device Support**
  - Input devices (X0-X7) with octal addressing
  - Output devices (Y0-Y7) with octal addressing
  - Internal relays (M devices) with decimal addressing
  - Latching relays (L devices) with decimal addressing
  - Timer and counter coil support (T, C devices)
  - Data register support (D devices)

- **Instruction Set Coverage**
  - All basic logical operations
  - Inverted operations (LDI, ANI, ORI, XORI, OUTI)
  - Set and reset operations
  - Block operations (ANB, ORB) with stack management
  - Pulse operations (PLS, PLF) - framework ready

### Known Limitations
- Timer and counter preset/execution not yet implemented
- Data transfer instructions (MOV, DMOV) not implemented
- Jump and subroutine instructions not implemented
- No program stepping or breakpoint support
- Limited to 8 inputs/outputs in current GUI

### Installation Requirements
- Java 11 or higher with JavaFX modules
- Leiningen build tool for Clojure
- Platform-specific JavaFX module configuration

This initial release provides a solid foundation for Mitsubishi IL program simulation with a focus on logical operations and basic PLC functionality. The modular architecture supports easy extension for additional instruction types and enhanced simulation features.
