package br.gui.cc.gen.ex;

import java.util.HashMap;
import java.util.Map;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.semantic.SymbolTableVariable;

/**
program
  0: command+ 
command+
  0: command 
  1: command command+ 
command
  0: identifier attribution expression semicolon 
  1: print parentheses_open identifier parentheses_close semicolon 
expression
  0: term plus expression 
  1: term minus expression 
  2: plus expression 
  3: minus expression 
  4: term 
term
  0: factor mult term 
  1: factor div term 
  2: factor
factor
  0: integer
  1: parentheses_open expression parentheses_close 
 */
public class MathVarGenerator extends SimpleGenerator {
	private static final String VAR_PREFIX = "id_";
	
	public MathVarGenerator() {
		load(null);
	}

	@Override
	public void load(Map<String, ICoder> map) {
		coderMap = new HashMap<String, ICoder>();
		coderMap.put("0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				String code = ".data\n";
				for (SymbolTableVariable symbolVar : symbolTable.getVarList()) {
					code += VAR_PREFIX+symbolVar.getLexeme()+": .word 0\n";
				}
				code += ".text\nmain:\n";
				code += generateNode(node);
				code += "jr $ra";
				return code;
			}
		});
		// command0: identifier attribution expression semicolon
		coderMap.put("command0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				String code = generateNode(node.getChildren().get(2));
				code += "sw $a0 "+generateNode(node.getChildren().get(0))+"\n";
				return code;
			}
		});
		// command1: print parentheses_open expression parentheses_close semicolon
		coderMap.put("command1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				String result = generateNode(node.getChildren().get(2))
						+ "li $v0 1\n"
						+ "syscall\n";
				return result;
			}
		});
		// expression0: term plus expression 
		coderMap.put("expression0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0))
						+ "sw $a0 0($sp)\n"
						+ "addiu $sp $sp -4\n"
						+ generateNode(node.getChildren().get(2))
						+ "lw $t0 4($sp)\n"
						+ "add $a0 $t0 $a0\n"
						+ "addiu $sp $sp 4\n";
			}
		});
		// expression1: term minus expression 
		coderMap.put("expression1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0))
						+ "sw $a0 0($sp)\n"
						+ "addiu $sp $sp -4\n"
						+ generateNode(node.getChildren().get(2))
						+ "lw $t0 4($sp)\n"
						+ "sub $a0 $t0 $a0\n"
						+ "addiu $sp $sp 4\n";
			}
		});
		// expression2: plus expression
		coderMap.put("expression2", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(1));
			}
		});
		// expression3: minus expression
		coderMap.put("expression3", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(1))
						+ "mult $a0 -1\n"
						+ "mflo $a0\n";
			}
		});
		// expression4: term
		coderMap.put("expression4", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0));
			}
		});
		// term0: factor mult term
		coderMap.put("term0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0))
						+ "sw $a0 0($sp)\n"
						+ "addiu $sp $sp -4\n"
						+ generateNode(node.getChildren().get(2))
						+ "lw $t0 4($sp)\n"
						+ "mult $a0 $t0\n"
						+ "mflo $a0\n"
						+ "addiu $sp $sp 4\n";
			}
		});
		// term1: factor div term
		coderMap.put("term1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0))
						+ "sw $a0 0($sp)\n"
						+ "addiu $sp $sp -4\n"
						+ generateNode(node.getChildren().get(2))
						+ "lw $t0 4($sp)\n"
						+ "div $t0 $a0\n"
						+ "mflo $a0\n"
						+ "addiu $sp $sp 4\n";
			}
		});
		// term2: factor
		coderMap.put("term2", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0));
			}
		});
		// factor0: integer
		coderMap.put("factor0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0));
			}
		});
		// factor1: identifier
		coderMap.put("factor1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return "lw $a0 " + generateNode(node.getChildren().get(0)) + "\n";
			}
		});
		// factor2: parentheses_open expression parentheses_close
		coderMap.put("factor2", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(1));
			}
		});

		coderMap.put("integer", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return "li $a0 "+node.getToken().getLexeme()+"\n";
			}
		});
		coderMap.put("identifier", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return VAR_PREFIX+node.getToken().getLexeme();
			}
		});
	}

}
