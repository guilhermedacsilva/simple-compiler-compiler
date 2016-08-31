package br.gui.cc.gen;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.gui.cc.all.DynamicCompiler;
import br.gui.cc.all.DynamicCompilerBuilder;
import br.gui.cc.all.TestDynamicCompilerShortcut;
import br.gui.cc.gen.reader.GeneratorReader;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.parser.slr.TestSLRParser;
import br.gui.cc.semantic.SimpleSemantic;
import br.gui.cc.semantic.TypeReader;

public class TestLang1MathVar {
	private static DynamicCompiler compiler;

	@BeforeClass
	public static void createCompiler() {
		DynamicCompilerBuilder builder = new DynamicCompilerBuilder();
		builder.setLexerFile("test_resources/lang_1_math_var/lexer")
			.setParserFile("test_resources/lang_1_math_var/parser")
			.setSemanticFile("test_resources/lang_1_math_var/semantic")
			.setSymbolTableFile("test_resources/lang_1_math_var/symbol_table")
			.setGenFile("test_resources/lang_1_math_var/gen");
		compiler = builder.build();
	}
	
	@Test
	public void testVarMath2() throws Exception {
		String programFile = "test_resources/lang_1_math_var/program_2";
		String[] vars = {"a"};
		TestDynamicCompilerShortcut.test(compiler, programFile, vars);
	}

	@Test
	public void testVarMath3() throws Exception {
		String programFile = "test_resources/lang_1_math_var/program_2";
		String[] vars = {"a","b"};
		TestDynamicCompilerShortcut.test(compiler, programFile, vars);
	}
	
	@Test
	public void testMath() throws Exception {
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
		parser.processTokens(tokenList);
		
//		TestAST.print(parser.getAbstractSintaxTree(), true);
		
		SimpleSemantic semanticParser = new SimpleSemantic(parser.getAbstractSintaxTree());
		Map<String, Object> semanticMap = TypeReader.createSemanticMap(new FileReader("test_resources/lang_2_math/semantic"));
		semanticParser.setConfiguration(semanticMap);
		assertTrue(semanticParser.check());
		
		SimpleGenerator generator = new SimpleGenerator();
		GeneratorReader generatorReader = new GeneratorReader();
		generator.load(generatorReader.createCoderMap(new FileReader("test_resources/lang_2_math/gen")));
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
				+ "jr $ra", code.trim());
//		System.out.print(code);
	}

}
