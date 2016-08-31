package br.gui.cc.parser.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class TestFirstFollowUtil {

	/**
	 * E -> T E' 				-- firsts: int -- ok
	 * E' -> +T | epsilon 		-- firsts: + | epsilon -- ok
	 * T -> int T' 				-- firsts: int -- ok
	 * T' -> * T | epsilon		-- firsts: * | epsilon
	 */
	@Test
	public void test() {
		Production prodE = new Production();
		prodE.add(Symbol.nonTerminal("T"));
		prodE.add(Symbol.nonTerminal("E1"));
		Production prodE1 = new Production();
		prodE1.add(Symbol.terminal("+"));
		prodE1.add(Symbol.nonTerminal("T"));
		Production prodT = new Production();
		prodT.add(Symbol.terminal("int"));
		prodT.add(Symbol.nonTerminal("T1"));
		Production prodT1 = new Production();
		prodT1.add(Symbol.terminal("*"));
		prodT1.add(Symbol.nonTerminal("T"));
		Production prodEpsilon = new Production();
		prodEpsilon.add(Symbol.EPSILON);
		
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
		 * E -> T E' 				-- firsts: int -- ok
		 * E' -> +T | epsilon 		-- firsts: + | epsilon -- ok
		 * T -> int T' 				-- firsts: int -- ok
		 * T' -> * T | epsilon		-- firsts: * | epsilon
		 * 
		 * 		FOLLOW
		 * E	$
		 * E'	$
		 * T	+ $
		 * T'	+ $
		 * */
		FirstFollowUtil ffUtil = new FirstFollowUtil(groups);
		assertEquals(1, ffUtil.firstSets.get("E").size());
		assertTrue(ffUtil.firstSets.get("E").contains(Symbol.terminal("int")));
		assertEquals(2, ffUtil.firstSets.get("E1").size());
		assertTrue(ffUtil.firstSets.get("E1").contains(Symbol.terminal("+")));
		assertTrue(ffUtil.firstSets.get("E1").contains(Symbol.EPSILON));
		assertEquals(1, ffUtil.firstSets.get("T").size());
		assertTrue(ffUtil.firstSets.get("T").contains(Symbol.terminal("int")));
		assertEquals(2, ffUtil.firstSets.get("T1").size());
		assertTrue(ffUtil.firstSets.get("T1").contains(Symbol.terminal("*")));
		assertTrue(ffUtil.firstSets.get("T1").contains(Symbol.EPSILON));

		assertEquals(1, ffUtil.followSets.get("E").size());
		assertTrue(ffUtil.followSets.get("E").contains(Symbol.END));
		assertEquals(1, ffUtil.followSets.get("E1").size());
		assertTrue(ffUtil.followSets.get("E1").contains(Symbol.END));
		assertEquals(2, ffUtil.followSets.get("T").size());
		assertTrue(ffUtil.followSets.get("T").contains(Symbol.END));
		assertTrue(ffUtil.followSets.get("T").contains(Symbol.terminal("+")));
		assertEquals(2, ffUtil.followSets.get("T1").size());
		assertTrue(ffUtil.followSets.get("T1").contains(Symbol.END));
		assertTrue(ffUtil.followSets.get("T1").contains(Symbol.terminal("+")));
	}

}
