.data
math_var_a: .word 0
.text
main:
li $a0 -3
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 5
lw $t0 4($sp)
mult $a0 $t0
mflo $a0
addiu $sp $sp 4
sw $a0 math_var_a
li $v0, 1
lw $a0 math_var_a
syscall
jr $ra