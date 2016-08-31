package br.gui.cc.parser.reader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.TestProductionGroup;

public class TestECFGReader {

	@Test
	public void test() throws Exception {
		ECFGReader reader = new ECFGReader();
		reader.process(new FileReader("test_resources/parser_ecfg"));
		List<ProductionGroup> groups = reader.getProductionGroups();
		
		List<ProductionGroup> groupsExpected = new ArrayList<ProductionGroup>(2);
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"class1", 
						Symbol.nonTerminal("a+"),
						null, null, null));
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"class2", 
						Symbol.nonTerminal("b?"),
						null, null, null));
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"class3", 
						Symbol.nonTerminal("c*"),
						null, null, null));
		
		ProductionGroup group = TestProductionGroup.createGroup(
				"a+", 
				Symbol.terminal("a"),
				null, null, null);
		group.add(TestProductionGroup.createProduction(
				Symbol.terminal("a"),
				Symbol.nonTerminal("a+"),
				null, null));
		groupsExpected.add(group);
		
		group = TestProductionGroup.createGroup(
				"b?", 
				Symbol.EPSILON,
				null, null, null);
		group.add(TestProductionGroup.createProduction(
				Symbol.terminal("b"),
				null, null, null));
		groupsExpected.add(group);
		
		group = TestProductionGroup.createGroup(
				"c*", 
				Symbol.EPSILON,
				null, null, null);
		group.add(TestProductionGroup.createProduction(
				Symbol.terminal("c"),
				Symbol.nonTerminal("c*"),
				null, null));
		groupsExpected.add(group);
		
		TestProductionGroup.assertGroupList(groupsExpected, groups);
	}

}
