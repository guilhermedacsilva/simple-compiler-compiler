package br.gui.cc.parser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TestProductionGroup {
	
	public static void print(List<ProductionGroup> groupList) {
		Production production;
		int pi;
		for (ProductionGroup group : groupList) {
			System.out.println(group.getName());
			for (pi = 0; pi < group.size(); pi++) {
				production = group.get(pi);
				System.out.print("  "+pi+": ");
				for (Symbol symbol : production) {
					System.out.print(symbol + " ");
				}
				System.out.print("\n");
			}
		}
	}
	
	public static ProductionGroup createGroup(String name, 
			Symbol s1,
			Symbol s2,
			Symbol s3,
			Symbol s4) {
		
		ProductionGroup group = new ProductionGroup(name);
		group.add(createProduction(s1, s2, s3, s4));
		return group;
	}
	
	public static Production createProduction(
			Symbol s1,
			Symbol s2,
			Symbol s3,
			Symbol s4) {
		
		Production prod = new Production();
		if (s1 != null) prod.add(s1);
		if (s2 != null) prod.add(s2);
		if (s3 != null) prod.add(s3);
		if (s4 != null) prod.add(s4);
		return prod;
	}

	public static void assertGroupList(
			List<ProductionGroup> groupListExp,
			List<ProductionGroup> groupListAct) {
		
		assertEquals(groupListExp.size(), groupListAct.size());
		boolean found;
		for (ProductionGroup groupExp : groupListExp) {
			found = false;
			for (ProductionGroup groupAct : groupListAct) {
				if (groupExp.getName().equals(groupAct.getName())) {
					found = true;
					assertGroup(groupExp, groupAct);
					break;
				}
			}
			if (!found) {
				fail("Group " + groupExp.getName() + " is missing.");
			}
		}
	}

	private static void assertGroup(
			ProductionGroup groupExp,
			ProductionGroup groupAct) {
		
		assertEquals(groupExp.size(), groupAct.size());
		boolean found;
		for (Production prodExp : groupExp) {
			found = false;
			for (Production prodAct : groupAct) {
				found = tryMatch(prodExp, prodAct);
				if (found) break;
			}
			if (!found) productionNotFound(groupExp, prodExp);
		}
	}

	private static boolean tryMatch(Production prodExp, Production prodAct) {
		if (prodExp.size() != prodAct.size()) {
			return false;
		}
		for (int i = 0; i < prodExp.size(); i++) {
			if (!prodExp.get(i).equals(prodAct.get(i))) {
				return false;
			}
		}
		return true;
	}

	private static void productionNotFound(
			ProductionGroup group, Production prod) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Production not found!\nGroup: ")
			.append(group.getName())
			.append("\nProduction:\n");
		for (Symbol symbol : prod) {
			sb.append("\tvalue: ").append(symbol.getValue())
				.append("\n\tterminal: ")
				.append(symbol.isTerminal() ? "true" : "false");						
		}
		fail(sb.toString());
	}
	

}
