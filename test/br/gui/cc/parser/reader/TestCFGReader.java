package br.gui.cc.parser.reader;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.TestProductionGroup;

public class TestCFGReader {

	@Test
	public void test1() throws Exception {
		CFGReader reader = new CFGReader();
		reader.process(new FileReader("test_resources/parser_cfg_1"));
		List<ProductionGroup> groups = reader.getProductionGroups();
		
		List<ProductionGroup> groupsExpected = new ArrayList<ProductionGroup>(2);
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"classe1", 
						Symbol.terminal("1"),
						null, null, null));
		
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"classe2", 
						Symbol.terminal("abc"),
						Symbol.terminal(";"), 
						null, null));
		
		TestProductionGroup.assertGroupList(groupsExpected, groups);
	}

	@Test
	public void test2() throws Exception {
		CFGReader reader = new CFGReader();
		reader.process(new FileReader("test_resources/parser_cfg_2"));
		List<ProductionGroup> groups = reader.getProductionGroups();
		
		List<ProductionGroup> groupsExpected = new ArrayList<ProductionGroup>(2);
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"classe1", 
						Symbol.nonTerminal("classe2+"),
						null, null, null));
		
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"classe2",
						Symbol.terminal("a"),
						null, null, null));

		ProductionGroup group = TestProductionGroup.createGroup(
				"classe2?",
				Symbol.nonTerminal("classe2"), 
				null, null, null);
		group.add(TestProductionGroup.createProduction(
				Symbol.EPSILON, null, null, null));
		groupsExpected.add(group);

		group = TestProductionGroup.createGroup(
				"classe2*",
				Symbol.nonTerminal("classe2"), 
				Symbol.nonTerminal("classe2*"), 
				null, null);
		group.add(TestProductionGroup.createProduction(
				Symbol.EPSILON, null, null, null));
		groupsExpected.add(group);
		
		groupsExpected.add(
				TestProductionGroup.createGroup(
						"classe2+",
						Symbol.nonTerminal("classe2"),
						Symbol.nonTerminal("classe2*"), 
						null, null));
		
		TestProductionGroup.assertGroupList(groupsExpected, groups);
	}

}
