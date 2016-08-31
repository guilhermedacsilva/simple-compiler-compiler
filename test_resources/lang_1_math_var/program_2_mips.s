.data
id_a: .word 0
.text
main:
li $a0 1
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 2
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 3
lw $t0 4($sp)
mult $a0 $t0
mflo $a0
addiu $sp $sp 4
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 2
lw $t0 4($sp)
div $t0 $a0
mflo $a0
addiu $sp $sp 4
lw $t0 4($sp)
add $a0 $t0 $a0
addiu $sp $sp 4
sw $a0 id_a
li $a0 55
sw $a0 id_a
lw $a0 id_a
li $v0 1
syscall
jr $ra