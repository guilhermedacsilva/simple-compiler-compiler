package br.gui.cc.parser.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class TestParsingTable {

	/**
	 * E -> ( E ) | epsilon
	 * 
	 * 		(		)		$
	 * E	(E)		ep		ep
	 */
	@Test
	public void test() {
		List<ProductionGroup> groups = new ArrayList<ProductionGroup>(1);
		
		ProductionGroup group = new ProductionGroup("E");
		Production prod1 = new Production();
		prod1.add(Symbol.terminal("("));
		prod1.add(Symbol.nonTerminal("E"));
		prod1.add(Symbol.terminal(")"));
		group.add(prod1);
		
		Production prod2 = new Production();
		prod2.add(Symbol.EPSILON);
		group.add(prod2);
		
		groups.add(group);
		
		FirstFollowUtil ffUtil = new FirstFollowUtil(groups);
		assertTrue(ffUtil.firstSets.containsKey("E"));
		assertEquals(2, ffUtil.firstSets.get("E").size());
		assertTrue(ffUtil.firstSets.get("E").contains(Symbol.terminal("(")));
		assertTrue(ffUtil.firstSets.get("E").contains(Symbol.EPSILON));
		
		assertTrue(ffUtil.followSets.containsKey("E"));
		assertEquals(2, ffUtil.followSets.get("E").size());
		assertTrue(ffUtil.followSets.get("E").contains(Symbol.terminal(")")));
		assertTrue(ffUtil.followSets.get("E").contains(Symbol.END));
		
		ParsingTable table = new ParsingTable(groups);
		assertTrue(table.getTable().containsKey("E"));
		assertEquals(prod1, table.getTable().get("E").get("("));
		assertEquals(prod2, table.getTable().get("E").get(")"));
		assertEquals(prod2, table.getTable().get("E").get("$"));
		
		
	}

}
