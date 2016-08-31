package br.gui.cc.gen.ex;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.ex.MathVarGenerator;
import br.gui.cc.gen.reader.GeneratorReader;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;
import br.gui.cc.parser.reader.ECFGReader;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.semantic.SimpleSemantic;
import br.gui.cc.semantic.SymbolTable;
import br.gui.cc.semantic.SymbolTableReader;
import br.gui.cc.semantic.TypeReader;

public class TestMathVarGenerator {
	private boolean shouldPrint;
	
	private void printTokenList(List<Token> tokenList) {
		System.out.println("Token list:");
		for (Token token : tokenList) {
			System.out.println("\t"+token);
		}
	}
	
	private String generateCode(String programFile) throws Exception {
		SimpleLexer lexer = (SimpleLexer) SimpleLexerBuilder.initLexer(
				new SimpleLexer(), 
				new FileReader("test_resources/lang_1_math_var/lexer"));
		
		lexer.setSourceCode(new FileReader(programFile));
		List<Token> tokenList = lexer.getAllTokens();
		
		if (shouldPrint) printTokenList(tokenList);
		
		List<ProductionGroup> productionGroups = 
				ECFGReader.processECFG(new FileReader("test_resources/lang_1_math_var/parser"));
		
		if (shouldPrint) TestProductionGroup.print(productionGroups); // PRINT
		
		SLRParser parser = new SLRParser();
		parser.loadCFG(productionGroups);
		assertTrue(parser.processTokens(tokenList));
		
		if (shouldPrint) TestAST.print(parser.getAbstractSintaxTree(), true); // PRINT
		
		SimpleSemantic semanticParser = new SimpleSemantic(parser.getAbstractSintaxTree());
		Map<String, Object> semanticMap = 
				TypeReader.createSemanticMap(new FileReader("test_resources/lang_1_math_var/semantic"));
		semanticParser.setConfiguration(semanticMap);
		assertTrue(semanticParser.check());
		
		HashMap<String, String> symbolMap = 
				SymbolTableReader.createSymbolMap(new FileReader("test_resources/lang_1_math_var/symbol_table"));
		SymbolTable symbolTable = new SymbolTable(symbolMap);
		symbolTable.processAST(parser.getAbstractSintaxTree());

		assertEquals(1, symbolTable.getVarList().size());
		assertEquals("a", symbolTable.getVarList().get(0).getLexeme());
		
		MathVarGenerator generator = new MathVarGenerator();
		String code = generator.generateAll(parser.getAbstractSintaxTree(), symbolTable);

		if (shouldPrint) System.out.print(code); // PRINT
		
		return code;
	}

	@Test
	public void test() throws Exception {
		shouldPrint = false;
		String generatedCode = generateCode("test_resources/lang_1_math_var/program_1");
				
		String expectedCode =
				new String(Files.readAllBytes(Paths.get("test_resources/lang_1_math_var/program_1_mips.s")));
		
		assertEquals(expectedCode.replaceAll("\r", ""), generatedCode);
		
//		shouldPrint = true;
		generatedCode = generateCode("test_resources/lang_1_math_var/program_2");
		
		expectedCode =
				new String(Files.readAllBytes(Paths.get("test_resources/lang_1_math_var/program_2_mips.s")));
		
		assertEquals(expectedCode.replaceAll("\r", ""), generatedCode);
	}

}