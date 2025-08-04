; Traffic Light Controller
; Simple 3-state traffic light simulation
; X0 = Start/Stop
; X1 = Manual advance (for testing)
; Y0 = Red Light
; Y1 = Yellow Light  
; Y2 = Green Light

; Start condition - if X0 is on, start the sequence
LD X0
SET M10    ; Set "running" flag

; Red light state (default)
LD M10     ; If running
OUT Y0     ; Turn on red light

; Manual advance or automatic progression would go here
; For this simple example, we'll use X1 to manually control yellow
LD X1      ; Manual yellow control
AND M10    ; Only if system is running
OUT Y1     ; Turn on yellow light

; Green light (inverted yellow for simplicity)
LD X1      ; Manual control
AND M10    ; Only if system is running
OUTI Y2    ; Inverted output to green (opposite of yellow)

; Stop condition
LD X0
OUTI M10   ; Clear running flag when X0 is off
