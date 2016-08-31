.data
theString: .space 64

.text
main:
    li      $v0, 8
    la      $a0, theString
    li      $a1, 64
    syscall
    li      $v0, 4
    syscall
    jr      $ra