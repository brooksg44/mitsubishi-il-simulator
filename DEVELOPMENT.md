)
- [Clojure Style Guide](https://guide.clojure.style/)

## Build and Distribution

### Creating Releases

1. **Update Version**
   ```clojure
   ;; In project.clj
   (defproject mitsubishi-il-simulator "0.2.0"
     ;; ... rest of config
   ```

2. **Build Uberjar**
   ```bash
   lein clean
   lein uberjar
   ```

3. **Test Distribution**
   ```bash
   java -jar target/uberjar/mitsubishi-il-simulator-0.2.0-standalone.jar
   ```

### Platform-Specific Packaging

#### Windows
- Use Launch4j to create .exe wrapper
- Include JavaFX runtime in distribution
- Create installer with NSIS or similar

#### macOS
- Create .app bundle with custom Info.plist
- Code sign for distribution
- Use DMG for distribution packaging

#### Linux
- Create .deb/.rpm packages
- Include desktop file for menu integration
- Handle JavaFX dependencies

## Performance Optimization

### Profiling Tools
```bash
# Memory profiling
java -XX:+PrintGC -XX:+PrintGCDetails -jar simulator.jar

# CPU profiling with VisualVM
jvisualvm --jdkhome $JAVA_HOME

# Flight Recorder
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=profile.jfr -jar simulator.jar
```

### Optimization Techniques

1. **Parser Optimization**
   ```clojure
   ;; Cache parser instance
   (def ^:private cached-parser (delay (insta/parser il-grammar)))
   
   (defn parse-il [text]
     (let [result (@cached-parser text)]
       ;; ... rest of function
   ```

2. **State Management**
   ```clojure
   ;; Use transients for bulk updates
   (defn bulk-update-devices [updates]
     (swap! plc-state 
       (fn [state]
         (reduce (fn [s [device-type addr val]]
                   (assoc-in s [device-type addr] val))
                 state
                 updates))))
   ```

3. **GUI Optimization**
   ```clojure
   ;; Debounce frequent updates
   (def update-debouncer (atom nil))
   
   (defn debounced-update [f delay]
     (when @update-debouncer
       (.cancel @update-debouncer))
     (reset! update-debouncer 
       (future
         (Thread/sleep delay)
         (f))))
   ```

## Security Considerations

### File Operations
- Validate file paths to prevent directory traversal
- Sanitize file contents before parsing
- Implement file size limits

### Code Execution
- Parser only generates data structures, no code execution
- Simulator runs in controlled environment
- No network or system access from IL programs

### Input Validation
```clojure
(defn safe-parse-int [s]
  (try
    (Integer/parseInt (str s))
    (catch NumberFormatException _
      nil)))

(defn validate-device-address [device-type address]
  (and (integer? address)
       (>= address 0)
       (case device-type
         (:input :output) (< address 8)  ; Octal 0-7
         (:memory :latch) (< address 1000)
         true)))
```

## Maintenance Tasks

### Regular Maintenance
- Update dependencies quarterly
- Review and update documentation
- Run security scans on dependencies
- Performance regression testing

### Dependency Updates
```bash
# Check for outdated dependencies
lein ancient

# Update specific dependency
# Edit project.clj, then:
lein deps

# Test after updates
lein test
lein run
```

### Code Quality
```bash
# Linting with eastwood
lein eastwood

# Code formatting with cljfmt
lein cljfmt check
lein cljfmt fix

# Dependency analysis
lein deps :tree
```

## Integration Points

### External Tool Integration

1. **IDE Integration**
   - Syntax highlighting for .il files
   - REPL integration for development
   - Debugger support

2. **Version Control**
   - .gitignore for build artifacts
   - Pre-commit hooks for code quality
   - Continuous integration setup

3. **Build Systems**
   - Maven repository publication
   - Docker containerization
   - Automated testing pipelines

### API Extensions
```clojure
;; Programmatic API for automation
(ns mitsubishi-il-simulator.api
  (:require [mitsubishi-il-simulator.simulator :as sim]))

(defn create-simulator []
  "Create new simulator instance"
  ;; Return simulator state/handle
  )

(defn load-program-from-string [sim program-text]
  "Load IL program from string"
  ;; Implementation
  )

(defn set-input-values [sim input-map]
  "Set multiple inputs at once"
  ;; Implementation
  )
```

## Documentation Standards

### Code Documentation
- Every public function has docstring
- Complex algorithms have inline comments
- Architecture decisions documented in ADR format

### User Documentation
- Step-by-step tutorials for common tasks
- Complete instruction reference
- Troubleshooting guide with solutions

### API Documentation
```clojure
(defn execute-instruction
  "Execute a single IL instruction.
  
  Args:
    instruction - Parsed instruction vector from parser
    
  Returns:
    nil - Side effects only (updates PLC state)
    
  Throws:
    IllegalArgumentException - Invalid instruction format
    
  Example:
    (execute-instruction [:logical_instruction \"LD\" [:input_address \"0\"]])"
  [instruction]
  ;; Implementation
  )
```

## Community and Support

### Issue Reporting
- GitHub Issues with templates
- Bug report format standardization
- Feature request process

### Contributing Process
1. Fork repository
2. Create feature branch
3. Implement changes with tests
4. Submit pull request
5. Code review process
6. Merge and release

### Release Process
1. Version bump and changelog update
2. Full test suite execution
3. Build verification on all platforms
4. Tag release in version control
5. Publish artifacts to repositories
6. Update documentation websites
7. Announce release to community

This development guide provides a comprehensive foundation for maintaining and extending the Mitsubishi IL Simulator. Regular updates to this document ensure it remains current with the evolving codebase and development practices.
