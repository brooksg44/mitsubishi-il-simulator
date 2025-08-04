; Complex Logic with ANB/ORB Example
; Demonstrates parallel branches and block operations

; First branch: X0 AND X1
LD X0      ; Load X0
AND X1     ; AND with X1

; Second branch: X2 OR X3  
LD X2      ; Load X2 (starts new branch)
OR X3      ; OR with X3

; Combine branches with ORB
ORB        ; OR the two branches together
OUT Y0     ; Output result to Y0

; Another example with ANB
; Branch 1: (X0 OR X1)
LD X0      ; Load X0
OR X1      ; OR with X1

; Branch 2: (X2 AND X3)
LD X2      ; Load X2 (starts new branch)  
AND X3     ; AND with X3

; Combine with AND
ANB        ; AND the two branches together
OUT Y1     ; Output result to Y1
