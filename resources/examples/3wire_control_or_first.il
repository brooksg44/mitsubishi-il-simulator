; 3-Wire Control Circuit (Start-Stop with Holding Contact)
; Classic motor control circuit used in industrial applications
;
; INPUTS:
; X0 = Stop Button (Normally Closed) - Press to stop
; X1 = Start Button (Normally Open) - Press to start
;
; OUTPUTS:
; Y0 = Motor Contactor/Relay
;
; LOGIC: Motor runs when NOT stopped AND (start pressed OR motor already running)
; Circuit: (NOT X0) AND (X1 OR Y0) = Y0
;
; OPERATION:
; 1. Press Start (X1) with Stop released → Motor starts (Y0 ON)
; 2. Release Start → Motor keeps running via holding contact (Y0 stays ON)
; 3. Press Stop (X0) → Motor stops immediately (Y0 OFF)
; 4. Must release Stop and press Start again to restart

LD X1      ; Start new branch: Load X1 (start button)
OR Y0      ; OR with Y0 (holding contact - motor feedback)
ANI X0     ; Load inverted X0 (stop button - normally closed)
OUT Y0     ; Output result to motor contactor

; Test scenarios:
; X0=OFF, X1=ON,  Y0=OFF → Y0=ON  (motor starts)
; X0=OFF, X1=OFF, Y0=ON  → Y0=ON  (motor holds)
; X0=ON,  X1=any, Y0=any → Y0=OFF (motor stops - stop has priority)
