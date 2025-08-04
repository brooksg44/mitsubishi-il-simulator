; Motor Control with Safety Interlocks
; X0 = Start Button
; X1 = Stop Button (Emergency Stop)
; X2 = Safety Door Closed
; X3 = Motor Overload (normally closed)
; Y0 = Motor Contactor
; Y1 = Status Light (Green = Running)
; Y2 = Fault Light (Red = Error)
; M0 = Motor Running Flag
; M1 = Fault Condition

; Start Logic: Start button AND Safety door closed AND No overload
LD X0      ; Start button
AND X2     ; Safety door closed
AND X3     ; No motor overload (NC contact)
SET M0     ; Set motor running flag

; Stop Logic: Stop button OR Door open OR Overload
LD X1      ; Stop button (emergency stop)
OR X2      ; Door open (inverted)
OUTI X3    ; Motor overload detected  
RST M0     ; Reset motor running flag

; Fault Detection
LD X2      ; Door open
OUTI X3    ; OR overload
ORB        ; Combine fault conditions
SET M1     ; Set fault flag

; Clear fault when conditions are good
LD X2      ; Door closed
AND X3     ; No overload
AND X1     ; Stop button released
ANI X1     ; (inverted stop button)
RST M1     ; Clear fault flag

; Motor Output: Running flag AND no faults
LD M0      ; Motor running flag
AND M1     ; No faults
ANI M1     ; (inverted fault flag)
OUT Y0     ; Motor contactor

; Status Lights
LD M0      ; Motor running
OUT Y1     ; Green status light

LD M1      ; Fault condition
OUT Y2     ; Red fault light
