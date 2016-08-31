.data
id_a: .word 0
id_b: .word 0
.text
main:
li $a0 20
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 5
lw $t0 4($sp)
mult $a0 $t0
mflo $a0
addiu $sp $sp 4
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 2
lw $t0 4($sp)
add $a0 $t0 $a0
addiu $sp $sp 4
sw $a0 id_a
li $a0 100
sw $a0 id_b
lw $a0 id_a
sw $a0 0($sp)
addiu $sp $sp -4
lw $a0 id_b
lw $t0 4($sp)
add $a0 $t0 $a0
addiu $sp $sp 4
sw $a0 id_a
lw $a0 id_a
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 20
lw $t0 4($sp)
add $a0 $t0 $a0
addiu $sp $sp 4
li $v0 1
syscall
jr $ra