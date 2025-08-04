; Simple Logic Example
; Input X0 controls Output Y0 (same state)
; Input X1 inverted controls Output Y1 (opposite state)

LD X0      ; Load input X0
OUT Y0     ; Output to Y0 (Y0 = X0)

LD X1      ; Load input X1
OUTI Y1    ; Inverted output to Y1 (Y1 = NOT X1)
           ; When X1 is OFF, Y1 is ON
           ; When X1 is ON, Y1 is OFF

; AND logic example
LD X0      ; Load X0
AND X1     ; AND with X1
OUT Y2     ; Output result to Y2

; OR logic example  
LD X0      ; Load X0
OR X1      ; OR with X1
OUT Y3     ; Output result to Y3
