package br.gui.cc.gen.ex;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.gui.cc.gen.ex.MathGenerator;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.parser.slr.TestSLRParser;
import br.gui.cc.semantic.SimpleSemantic;
import br.gui.cc.semantic.TypeReader;

public class TestMathGenerator {

	@Test
	public void test() throws Exception {
		List<Token> tokenList = new ArrayList<Token>(9);
		tokenList.add(new SimpleToken("1", "int", 1));
		tokenList.add(new SimpleToken("+", "+", 1));
		tokenList.add(new SimpleToken("5", "int", 1));
		tokenList.add(new SimpleToken("*", "*", 1));
		tokenList.add(new SimpleToken("(", "(", 1));
		tokenList.add(new SimpleToken("1", "int", 1));
		tokenList.add(new SimpleToken("+", "+", 1));
		tokenList.add(new SimpleToken("3", "int", 1));
		tokenList.add(new SimpleToken("*", "*", 1));
		tokenList.add(new SimpleToken("3", "int", 1));
		tokenList.add(new SimpleToken(")", ")", 1));

		List<ProductionGroup> productionGroups = TestSLRParser.createMathProductions();
		
		SLRParser parser = new SLRParser();
		parser.loadCFG(productionGroups);

//		TestProductionGroup.print(productionGroups); // PRINT
		
		parser.processTokens(tokenList);
		
//		TestAST.print(parser.getAbstractSintaxTree(), true);
		
		SimpleSemantic semanticParser = new SimpleSemantic(parser.getAbstractSintaxTree());
		Map<String, Object> semanticMap = TypeReader.createSemanticMap(new FileReader("test_resources/lang_2_math/semantic"));
		semanticParser.setConfiguration(semanticMap);
		assertTrue(semanticParser.check());
		
		MathGenerator generator = new MathGenerator();
		String code = generator.generateAll(parser.getAbstractSintaxTree(), null);
		assertEquals(".text\n"
				+ "main:\n"
				+ "li $a0 1\n"
				+ "sw $a0 0($sp)\n"
				+ "addiu $sp $sp -4\n"
				+ "li $a0 5\n"
				+ "sw $a0 0($sp)\n"
				+ "addiu $sp $sp -4\n"
				+ "li $a0 1\n"
				+ "sw $a0 0($sp)\n"
				+ "addiu $sp $sp -4\n"
				+ "li $a0 3\n"
				+ "sw $a0 0($sp)\n"
				+ "addiu $sp $sp -4\n"
				+ "li $a0 3\n"
				+ "lw $t0 4($sp)\n"
				+ "mult $a0 $t0\n"
				+ "mflo $a0\n"
				+ "addiu $sp $sp 4\n"
				+ "lw $t0 4($sp)\n"
				+ "add $a0 $t0 $a0\n"
				+ "addiu $sp $sp 4\n"
				+ "lw $t0 4($sp)\n"
				+ "mult $a0 $t0\n"
				+ "mflo $a0\n"
				+ "addiu $sp $sp 4\n"
				+ "lw $t0 4($sp)\n"
				+ "add $a0 $t0 $a0\n"
				+ "addiu $sp $sp 4\n"
				+ "li $v0, 1\n"
				+ "syscall\n"
				+ "jr $ra", code);
//		System.out.print(code);
	}

}
