; Set and Reset Example
; X0 sets M0, X1 resets M0
; M0 controls Y0

LD X0      ; Load input X0
SET M0     ; Set memory bit M0

LD X1      ; Load input X1  
RST M0     ; Reset memory bit M0

LD M0      ; Load memory bit M0
OUT Y0     ; Output M0 state to Y0

; Latching relay example
LD X2      ; Load input X2
SET L0     ; Set latch L0

LD X3      ; Load input X3
RST L0     ; Reset latch L0

LD L0      ; Load latch L0
OUT Y1     ; Output L0 state to Y1
