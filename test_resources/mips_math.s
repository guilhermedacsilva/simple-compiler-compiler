li $a0 1
sw $a0 0($sp)
addiu $sp $sp -4
li $a0 5
lw $t1 4($sp)
add $a0 $t1 $a0
addiu $sp $sp 4