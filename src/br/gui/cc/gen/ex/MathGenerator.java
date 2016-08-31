package br.gui.cc.gen.ex;

import java.util.HashMap;
import java.util.Map;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

/**
E
  T
    int int:1
  + +:+
  E
    T
      int int:2
      * *:*
      T
        int int:3
        * *:*
        T
          ( (:(
          E
            T
              int int:3
            + +:+
            E
              T
                int int:3
          ) ):)

 */
public class MathGenerator extends SimpleGenerator {

	public MathGenerator() {
		load(null);
	}
	
	/**
	 * E > T + E | T
	 * T > int * T | int | (E)
	 */
	@Override
	public void load(Map<String, ICoder> map) {
		coderMap = new HashMap<String, ICoder>();
		coderMap.put("0", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				String code = ".text\nmain:\n";
				code += generateNode(node);
				code += "li $v0, 1\nsyscall\njr $ra";
				return code;
			}
		});
		coderMap.put("E0", new ICoder() {
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
		coderMap.put("E1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0));
			}
		});
		coderMap.put("T0", new ICoder() {
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
		coderMap.put("T1", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(0));
			}
		});
		coderMap.put("T2", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return generateNode(node.getChildren().get(1));
			}
		});
		coderMap.put("int", new ICoder() {
			public String doCode(SimpleGenerator generator, ASTNode node) {
				return "li $a0 "+node.getToken().getLexeme()+"\n";
			}
		});
	}

}
