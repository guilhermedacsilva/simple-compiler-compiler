package br.gui.cc.parser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class FirstFollowUtil {
	private List<ProductionGroup> allGroups;
	protected HashMap<String, Set<Symbol>> firstSets;
	protected HashMap<String, Set<Symbol>> followSets;
	private List<String> firstOks;
	private List<String> followOks;
	
	public FirstFollowUtil(List<ProductionGroup> groups) {
		allGroups = groups;
		firstSets = new HashMap<String, Set<Symbol>>();
		followSets = new HashMap<String, Set<Symbol>>();
		firstOks = new ArrayList<String>(groups.size());
		followOks = new ArrayList<String>(groups.size());
		calculateFirstSets();
		calculateFollowSets();
	}

	/**
	 * Only for nonterminals
	 */
	public Set<Symbol> getFirstSet(Symbol symbol) {
		return firstSets.get(symbol.getValue());
	}

	/**
	 * Only for nonterminals
	 */
	public Set<Symbol> getFollowSet(String name) {
		return followSets.get(name);
	}
	
	private boolean isCycle(
			ProductionGroup group, List<ProductionGroup> cycleList) {
		
		for (ProductionGroup groupCalculated : cycleList) {
			if (groupCalculated.getName().equals(group.getName())) return true;
		}
		return false;
	}
	
	private ProductionGroup getGroup(Symbol symbol) {
		return ProductionGroup.findGroup(allGroups, symbol.getValue());
	}
	
	private void calculateFirstSets() {
		for (ProductionGroup group : allGroups) {
			firstSets.put(group.getName(), new HashSet<Symbol>());
			calculateFirstSet(
					firstSets.get(group.getName()),
					group, 
					new ArrayList<ProductionGroup>(allGroups.size()));
			firstOks.add(group.getName());
		}
	}
	
	private void calculateFirstSet(
			Set<Symbol> destinationSet,
			ProductionGroup currentGroup,
			List<ProductionGroup> cycleList
			) {
		
		if (isCycle(currentGroup, cycleList)) {
			return;
		}
		cycleList.add(currentGroup);
		if (firstOks.contains(currentGroup.getName())) {
			destinationSet.addAll(firstSets.get(currentGroup.getName()));
			return;
		}
		
		Symbol symbol;
		for (Production production : currentGroup) {
			symbol = production.get(0);
			if (symbol.isTerminal()) {
				destinationSet.add(symbol);
			} else {
				calculateFirstSet(destinationSet, getGroup(symbol), cycleList);
			}
		}
	}

	private void calculateFollowSets() {
		followSets.put(allGroups.get(0).getName(), new HashSet<Symbol>());
		followSets.get(allGroups.get(0).getName()).add(Symbol.END);
		for (ProductionGroup group : allGroups) {
			if (!followSets.containsKey(group.getName())) {
				followSets.put(group.getName(), new HashSet<Symbol>());
			}
			calculateFollowSet(
					followSets.get(group.getName()),
					group, 
					new ArrayList<ProductionGroup>(allGroups.size()));
			followOks.add(group.getName());
		}
	}
	
	private void calculateFollowSet(
			Set<Symbol> destinationSet,
			ProductionGroup currentGroup,
			List<ProductionGroup> cycleList
			) {
		
		if (isCycle(currentGroup, cycleList)) {
			return;
		}
		cycleList.add(currentGroup);
		if (followOks.contains(currentGroup.getName())) {
			destinationSet.addAll(followSets.get(currentGroup.getName()));
			return;
		}
		
		boolean found;
		for (ProductionGroup group : allGroups) {
			for (Production production : group) {
				found = false;
				for (Symbol symbol : production) {
					if (found) {
						found = false;
						if (symbol.isTerminal()) {
							destinationSet.add(symbol);
						} else {
							for (Symbol firstSymbol : firstSets.get(symbol.getValue())) {
								if (firstSymbol.isEpsilon()) {
									calculateFollowSet(destinationSet, getGroup(symbol), cycleList);
								} else {
									destinationSet.add(firstSymbol);
								}
							}
						}
					}
					
					if (!symbol.isTerminal() && 
							symbol.getValue().equals(currentGroup.getName())) {
						found = true;
					}
				}
				
				if (found) {
					calculateFollowSet(destinationSet, group, cycleList);
				}
			}
		}
	}
	
	/*
	private ParserDefinition parserDefinition;
	private Set<String> firstSet;
	private Set<String> followSet;
	private boolean hasEndSymbolInFollow;
	private boolean firstSetDone;
	private boolean followSetDone;

	public FirstFollowUtil(ParserDefinition parserDefinition) {
		this.parserDefinition = parserDefinition;
		firstSet = new HashSet<String>();
		followSet = new HashSet<String>();
	}

	public void markEndInFollow() {
		hasEndSymbolInFollow = true;
	}
	
	protected Set<String> getFirstSet() {
		return firstSet;
	}
	
	protected Set<String> getFollowSet() {
		return followSet;
	}
	
	private boolean controlCycle(ParserDefinition actual, List<ParserDefinition> calculatedDefinitions) {
		if (calculatedDefinitions.contains(actual)) {
			return true;
		}
		calculatedDefinitions.add(actual);
		return false;
	}


	private void insertToSet(Set<String> destinationSet, Set<String> sourceSet) {
		for (String symbol : sourceSet) {
			destinationSet.add(symbol);
		}
	}

	public void calculateFirstSet(List<ParserDefinition> definitionList) {
		List<ParserDefinition> calculatedFirst = new LinkedList<ParserDefinition>();
		List<ParserDefinition> calculatedFollow = new LinkedList<ParserDefinition>();
		calculateFirstSet(firstSet, parserDefinition, definitionList, calculatedFirst, calculatedFollow);
		firstSetDone = true;
	}

	public void calculateFollowSet(List<ParserDefinition> definitionList) {
		List<ParserDefinition> calculatedFirst = new LinkedList<ParserDefinition>();
		List<ParserDefinition> calculatedFollow = new LinkedList<ParserDefinition>();
		calculateFollowSet(followSet, parserDefinition, definitionList, calculatedFirst, calculatedFollow);
		followSetDone = true;
	}

	private void calculateFirstSet(Set<String> destinationSet,
			ParserDefinition actualDefinition,
			List<ParserDefinition> definitionList,
			List<ParserDefinition> calculatedFirst,
			List<ParserDefinition> calculatedFollow) {
		
		if (controlCycle(actualDefinition, calculatedFirst)) {
			return;
		}
		if (actualDefinition.getFirstFollowSet().firstSetDone) {
			insertToSet(destinationSet, actualDefinition.getFirstFollowSet().firstSet);
			return;
		}

		int j;
		ParserRuleItem item;
		for (ParserRule rule : actualDefinition.getRules()) {
			j = 0;
			do {
				item = rule.getItem(j);

				if (item.isTerminal()) {
					destinationSet.add(item.getName());
					
				} else {
					calculateFirstSet(
						destinationSet,
						item.getDefinition(),
						definitionList,
						calculatedFirst,
						calculatedFollow
						);
				}
			} while (++j < rule.getItems().size() && item.isModifierZero());

			if (j == rule.getItems().size() && item.isModifierZero()) {
				calculateFollowSet(
					destinationSet,
					item.getDefinition(),
					definitionList,
					calculatedFirst,
					calculatedFollow
					);

			}
		}
	}


	private void calculateFollowSet(Set<String> destinationSet,
			ParserDefinition actualDefinition,
			List<ParserDefinition> definitionList,
			List<ParserDefinition> calculatedFirst,
			List<ParserDefinition> calculatedFollow) {

		if (controlCycle(actualDefinition, calculatedFollow)) {
			return;
		}
		if (actualDefinition.getFirstFollowSet().followSetDone) {
			insertToSet(destinationSet, actualDefinition.getFirstFollowSet().followSet);
			return;
		}

		boolean foundItem;

		if (actualDefinition.getFirstFollowSet().hasEndSymbolInFollow) {
			destinationSet.add("$");
		}
		for (ParserDefinition def : definitionList) {
			for (ParserRule rule : def.getRules()) {
				foundItem = false;
				for (ParserRuleItem item : rule.getItems()) {

					if (foundItem) {
						foundItem = item.isModifierZero();
						if (item.isTerminal()) {
							destinationSet.add(item.getName());
							
						} else {
							calculateFirstSet(
								destinationSet, 
								item.getDefinition(),
								definitionList, 
								calculatedFirst,
								calculatedFollow);
						}
					}

					if (item.getDefinition() == actualDefinition) {
						foundItem = true;
					}
				}

				if (foundItem) {
					calculateFollowSet(
						destinationSet, 
						def,
						definitionList, 
						calculatedFirst,
						calculatedFollow);
				}
			}
		}
	}
	
	*/

}
