; Normal vs Inverted Output Example
; Demonstrates the difference between OUT and OUTI

; Normal Output: Y0 follows X0 exactly
LD X0      ; Load input X0 state
OUT Y0     ; Y0 = X0 (same state)

; Inverted Output: Y1 is opposite of X1  
LD X1      ; Load input X1 state
OUTI Y1    ; Y1 = NOT X1 (inverted state)

; Test behavior:
; X0=OFF, X1=OFF → Y0=OFF, Y1=ON
; X0=OFF, X1=ON  → Y0=OFF, Y1=OFF  
; X0=ON,  X1=OFF → Y0=ON,  Y1=ON
; X0=ON,  X1=ON  → Y0=ON,  Y1=OFF
