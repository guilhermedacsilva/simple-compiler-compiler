package br.gui.cc.parser.slr;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;

public class TestSLRParser {

	/**
	 * E > T + E | T
	 * T > int * T | int | (E)
	 */
	public static List<ProductionGroup> createMathProductions() {
		ProductionGroup groupE =
				TestProductionGroup.createGroup("E", 
						Symbol.nonTerminal("T"), 
						Symbol.terminal("+"), 
						Symbol.nonTerminal("E"), null);
		groupE.add(TestProductionGroup.createProduction(
				Symbol.nonTerminal("T"), null, null, null));
		
		ProductionGroup groupT =
				TestProductionGroup.createGroup("T", 
						Symbol.terminal("int"), 
						Symbol.terminal("*"), 
						Symbol.nonTerminal("T"), null);
		groupT.add(TestProductionGroup.createProduction(
				Symbol.terminal("int"), null, null, null));
		groupT.add(TestProductionGroup.createProduction(
				Symbol.terminal("("), 
				Symbol.nonTerminal("E"), 
				Symbol.terminal(")"), null));

		groupE.get(0).setName("E0");
		groupE.get(1).setName("E1");
		groupT.get(0).setName("T0");
		groupT.get(1).setName("T1");
		groupT.get(2).setName("T2");
		
		List<ProductionGroup> groupList = new ArrayList<ProductionGroup>(2);
		groupList.add(groupE);
		groupList.add(groupT);
		return groupList;
	}
	public static SLRParser createMathParser() {
		SLRParser parser = new SLRParser();
		parser.loadCFG(createMathProductions());
		return parser;
	}

	@Test
	public void test() {
		SLRParser parser = createMathParser();
		
		List<Token> tokenList = new ArrayList<Token>(9);
		
		// tokenList: )
		tokenList.add(new SimpleToken(")", ")", 1));
		assertFalse(parser.processTokens(tokenList));
		
		// tokenList: 1
		tokenList.clear();
		tokenList.add(new SimpleToken("1", "int", 1));
		assertTrue(parser.processTokens(tokenList));

		ASTNode ast = new ASTNode(Symbol.nonTerminal("E"), null);
		ASTNode node1 = new ASTNode(Symbol.nonTerminal("T"), ast);
		new ASTNode(Symbol.terminal("int"), node1);
		TestAST.assertAST(ast, parser.getAbstractSintaxTree());
		
		// tokenList: 1+2
		tokenList.add(new SimpleToken("+", "+", 1));
		tokenList.add(new SimpleToken("2", "int", 1));
		
		assertTrue(parser.processTokens(tokenList));
		
		ast = new ASTNode(Symbol.nonTerminal("E"), null);
		node1 = new ASTNode(Symbol.nonTerminal("T"), ast);
		new ASTNode(Symbol.terminal("int"), node1);
		new ASTNode(Symbol.terminal("+"), ast);
		ASTNode node2 = new ASTNode(Symbol.nonTerminal("E"), ast);
		node2 = new ASTNode(Symbol.nonTerminal("T"), node2);
		new ASTNode(Symbol.terminal("int"), node2);
		
		TestAST.assertAST(ast, parser.getAbstractSintaxTree());


		// tokenList: 1+2*3
		tokenList.add(new SimpleToken("*", "*", 1));
		tokenList.add(new SimpleToken("3", "int", 1));
		
		assertTrue(parser.processTokens(tokenList));
		
		/*
		 * E > T + E | T
		 * T > int * T | int | (E)
		 */
		
		/* E
		 * T	+	E
		 * T	+	T
		 * T	+	int		*		T
		 * int	+	int		*		int
		 */
		
		ast = new ASTNode(Symbol.nonTerminal("E"), null);
		node1 = new ASTNode(Symbol.nonTerminal("T"), ast);
		new ASTNode(Symbol.terminal("int"), node1);
		new ASTNode(Symbol.terminal("+"), ast);
		node1 = new ASTNode(Symbol.nonTerminal("E"), ast);
		node1 = new ASTNode(Symbol.nonTerminal("T"), node1);
		new ASTNode(Symbol.terminal("int"), node1);
		new ASTNode(Symbol.terminal("*"), node1);
		node1 = new ASTNode(Symbol.nonTerminal("T"), node1);
		new ASTNode(Symbol.terminal("int"), node1);

		TestAST.assertAST(ast, parser.getAbstractSintaxTree());

		// tokenList: 1+2*3)
		tokenList.add(new SimpleToken(")", ")", 1));
		
		try {
			parser.processTokens(tokenList);
			fail();
		} catch (RuntimeException e) {}

		// tokenList: 1+2*3*(3+3)
		tokenList.remove(tokenList.size()-1);
		tokenList.add(new SimpleToken("*", "*", 1));
		tokenList.add(new SimpleToken("(", "(", 1));
		tokenList.add(new SimpleToken("3", "int", 1));
		tokenList.add(new SimpleToken("+", "+", 1));
		tokenList.add(new SimpleToken("3", "int", 1));
		tokenList.add(new SimpleToken(")", ")", 1));
		
		assertTrue(parser.processTokens(tokenList));
//		TestAST.print(parser.getAbstractSintaxTree(), true);
	}

}
