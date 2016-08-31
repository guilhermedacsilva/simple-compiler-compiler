.data
id_a: .word 0
.text
main:
li $a0 3
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 5
lw $t0 4($sp)
mult $a0 $t0
mflo $a0
addiu $sp $sp 4
mult $a0 -1
mflo $a0
sw $a0 id_a
lw $a0 id_a
li $v0 1
syscall
jr $ra