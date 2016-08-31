package br.gui.cc.parser.rd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class TestRecursiveDescentParser {
	private RecursiveDescentParser parser = getParserReady();

	/**
	 * E -> ( E ) | epsilon
	 */
	private RecursiveDescentParser getParserReady() {
		List<ProductionGroup> groups = new ArrayList<ProductionGroup>(1);
		
		ProductionGroup group = new ProductionGroup("E");
		Production prod = new Production();
		prod.add(Symbol.terminal("("));
		prod.add(Symbol.nonTerminal("E"));
		prod.add(Symbol.terminal(")"));
		group.add(prod);
		
		prod = new Production();
		prod.add(Symbol.EPSILON);
		group.add(prod);
		groups.add(group);

		RecursiveDescentParser parser = new RecursiveDescentParser();
		parser.loadCFG(groups);
		return parser;
	}
	
	@Test
	public void test() {		
		List<Token> tokens = new ArrayList<Token>(4);
		tokens.add(new SimpleToken("(", "(", 0));
		tokens.add(new SimpleToken(")", ")", 0));
		
		assertTrue(parser.processTokens(tokens));
//		TestASTTester.print(parser.getAbstractSintaxTree(), false);
		
		tokens.add(new SimpleToken(")", ")", 0));
		assertFalse(parser.processTokens(tokens));
		
		tokens.clear();
		assertTrue(parser.processTokens(tokens));

		tokens.add(new SimpleToken("(", "(", 0));
		tokens.add(new SimpleToken("(", "(", 0));
		tokens.add(new SimpleToken(")", ")", 0));
		tokens.add(new SimpleToken(")", ")", 0));
		assertTrue(parser.processTokens(tokens));
	}

}
