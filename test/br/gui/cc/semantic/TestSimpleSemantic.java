package br.gui.cc.semantic;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.reader.ECFGReader;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.parser.slr.TestSLRParser;
import br.gui.cc.util.ReaderUtil;

public class TestSimpleSemantic {

	/**
	 * E > T + E | T
	 * T > int * T | int | (E)
	 */
	@Test
	public void test() throws Exception {
		List<Token> tokenList = new ArrayList<Token>(9);
		tokenList.add(new SimpleToken("1", "int", 1));
		tokenList.add(new SimpleToken("+", "+", 1));
		tokenList.add(new SimpleToken("6", "int", 1));

		SLRParser parser = TestSLRParser.createMathParser();
		parser.processTokens(tokenList);
		
		SimpleSemantic semanticParser = new SimpleSemantic(parser.getAbstractSintaxTree());
		Map<String, Object> semanticMap = TypeReader.createSemanticMap(new FileReader("test_resources/semantic_math"));
		semanticParser.setConfiguration(semanticMap);
		assertTrue(semanticParser.check());
	}

}
