package br.gui.cc.parser.ll;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;
import br.gui.cc.parser.util.ParsingTable;

public class TestLL1Parser {
	private ParsingTable table;
	
	private Production get(String row, String column) {
		return table.getTable().get(row).get(column);
	}

	/**
	 * E -> T E' 				-- firsts: int -- ok
	 * E' -> +T | epsilon 		-- firsts: + | epsilon -- ok
	 * T -> int T' 				-- firsts: int -- ok
	 * T' -> * T | epsilon
	 */
	@Test
	public void test() {
		Production prodE = TestProductionGroup.createProduction(
				Symbol.nonTerminal("T"),
				Symbol.nonTerminal("E1"),
				null, null);
		Production prodE1 = TestProductionGroup.createProduction(
				Symbol.terminal("+"),
				Symbol.nonTerminal("T"),
				null, null);
		Production prodT = TestProductionGroup.createProduction(
				Symbol.terminal("int"),
				Symbol.nonTerminal("T1"),
				null, null);
		Production prodT1 = TestProductionGroup.createProduction(
				Symbol.terminal("*"),
				Symbol.nonTerminal("T"),
				null, null);
		Production prodEpsilon = TestProductionGroup.createProduction(
				Symbol.EPSILON, null, null, null);
		
		ProductionGroup groupE = new ProductionGroup("E");
		groupE.add(prodE);
		
		ProductionGroup groupE1 = new ProductionGroup("E1");
		groupE1.add(prodE1);
		groupE1.add(prodEpsilon);
		
		ProductionGroup groupT = new ProductionGroup("T");
		groupT.add(prodT);
		
		ProductionGroup groupT1 = new ProductionGroup("T1");
		groupT1.add(prodT1);
		groupT1.add(prodEpsilon);
		
		List<ProductionGroup> groups = new ArrayList<ProductionGroup>(4);
		groups.add(groupE);
		groups.add(groupE1);
		groups.add(groupT);
		groups.add(groupT1);
		
		/*
		 * 		int		+		*		$
		 * E	T E'
		 * E'			+T				ep
		 * T	int T'
		 * T'			ep		* T		ep
		 */
		table = new ParsingTable(groups);
		assertTrue(table.getTable().containsKey("E"));
		assertEquals(prodE, get("E","int"));
		assertNull(get("E","+"));
		assertNull(get("E","*"));
		assertNull(get("E","$"));
		assertTrue(table.getTable().containsKey("E1"));
		assertNull(get("E1","int"));
		assertEquals(prodE1, get("E1","+"));
		assertNull(get("E1","*"));
		assertEquals(prodEpsilon, get("E1","$"));
		assertTrue(table.getTable().containsKey("T"));
		assertEquals(prodT, get("T","int"));
		assertNull(get("T","+"));
		assertNull(get("T","*"));
		assertNull(get("T","$"));
		assertTrue(table.getTable().containsKey("T1"));
		assertNull(get("T1","int"));
		assertEquals(prodEpsilon, get("T1","+"));
		assertEquals(prodT1, get("T1","*"));
		assertEquals(prodEpsilon, get("T1","$"));
		
		
		List<Token> tokens = new ArrayList<Token>(10);
		tokens.add(new SimpleToken("1", "int", 0));
		tokens.add(new SimpleToken("+", "+", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		tokens.add(new SimpleToken("*", "*", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		tokens.add(new SimpleToken("*", "*", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		
		LL1Parser parser = new LL1Parser();
		
		parser.loadCFG(groups);
		
		assertTrue(parser.processTokens(tokens));
		
//		TestAST.print(parser.getAbstractSintaxTree(), true);
	}

}
