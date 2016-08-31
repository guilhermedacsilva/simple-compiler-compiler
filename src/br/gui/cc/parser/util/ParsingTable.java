package br.gui.cc.parser.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class ParsingTable {
	private Map<String, Map<String, Production>> table;
	private FirstFollowUtil ffUtil; 
	
	public ParsingTable(List<ProductionGroup> groups) {
		table = new HashMap<String, Map<String,Production>>();
		ffUtil = new FirstFollowUtil(groups);
		
		for (ProductionGroup group : groups) {
			for (Production production : group) {
				insertCellsForProduction(group, production, ffUtil);
			}
		}
	}
	
	public Map<String, Map<String, Production>> getTable() {
		return table;
	}
	
	private void insertCellsForProduction(
			ProductionGroup group, Production production, FirstFollowUtil ffUtil) {
		if (production.get(0).isEpsilon()) {
			insertProductionFollow(group, production);
		} else {
			insertProductionFirst(group, production);
		}
	}

	private void insertProductionFollow(
			ProductionGroup group, Production production) {
		
		for (Symbol s : ffUtil.getFollowSet(group.getName())) {
			insertCell(group.getName(), s.getValue(), production);
		}
	}

	private void insertProductionFirst(
			ProductionGroup group, Production production) {
		
		if (production.get(0).isTerminal()) {
			insertCell(group.getName(), production.get(0).getValue(), production);
		} else {
			for (Symbol s : ffUtil.getFirstSet(production.get(0))) {
				insertCell(group.getName(), s.getValue(), production);
			}
		}
	}
	
	private void insertCell(
			String lineIndex, String columnIndex, Production production) {
		
		Map<String, Production> line = getTableLine(lineIndex);
		if (line.containsKey(columnIndex)) {
			throw new RuntimeException("Multiple entries in line ["
					+ lineIndex + "] and column [" + columnIndex + "]");
		}
		line.put(columnIndex, production);
	}

	private Map<String, Production> getTableLine(String name) {
		if (!table.containsKey(name)) {
			table.put(name, new HashMap<String, Production>());
		}
		return table.get(name);
	}
	
}
