package br.gui.cc.parser.slr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class NFAParser {
	private List<ProductionGroup> normalGroup;
	private AutomataState stateInitial;
	private List<AutomataState> statesAll;
	private List<AutomataState> statesOn;
	private List<AutomataState> statesSave;

	/**
	 * the group must already be augumented
	 */
	public NFAParser(List<ProductionGroup> groupList) {
		this.normalGroup = groupList;
	}
	
	public void reset() {
		statesOn.clear();
		statesOn.add(stateInitial);
		activateEpsilons();
		saveStates();
	}
	
	public void saveStates() {
		statesSave = new ArrayList<AutomataState>(statesOn);
	}
	
	public void restoreStates() {
		statesOn = new ArrayList<AutomataState>(statesSave);
	}
	
	public List<AutomataState> getStatesOn() {
		return statesOn;
	}

	private void activateEpsilons() {
		Map<Integer, AutomataState> statesReady = new HashMap<Integer, AutomataState>();
		Stack<AutomataState> statesToProcess = new Stack<AutomataState>();
		for (AutomataState stateOn : statesOn) {
			statesReady.put(stateOn.getId(), stateOn);
			statesToProcess.push(stateOn);
		}
		AutomataState state;
		while (!statesToProcess.isEmpty()) {
			state = statesToProcess.pop();
			for (AutomataState nextState : state.doTransition(Symbol.EPSILON)) {
				if (!statesReady.containsKey(nextState.getId())) {
					statesReady.put(nextState.getId(), nextState);
					statesToProcess.push(nextState);
				}
			}
		}
		statesOn.clear();
		statesOn = new ArrayList<AutomataState>(statesReady.values());
	}

	public boolean walk(Symbol s) {
		Map<Integer, AutomataState> stateMap = new HashMap<Integer, AutomataState>();
		List<AutomataState> transitionStates;
		for (AutomataState state : statesOn) {
			transitionStates = state.doTransition(s);
			for (AutomataState newState : transitionStates) {
				stateMap.put(newState.getId(), newState);
			}
		}
		statesOn.clear();
		statesOn = new ArrayList<AutomataState>(stateMap.values());
		activateEpsilons();
		return !statesOn.isEmpty();
	}
	
	public AutomataState getAutomata() {
		return stateInitial;
	}
	
	public void create() {
		statesAll = new LinkedList<AutomataState>();
		stateInitial = createInitialState();
		statesAll.add(stateInitial);
		createStatesFromState(stateInitial);
		statesOn = new LinkedList<AutomataState>();
		reset();
	}

	private AutomataState createInitialState() {
		Production initialProduction = new Production();
		initialProduction.add(SymbolDot.DOT);
		for (Symbol symbol : normalGroup.get(0).get(0)) {
			initialProduction.add(symbol);
		}
		initialProduction.setGroup(normalGroup.get(0));
		return new AutomataState(initialProduction, 0);
	}
	
	private void createStatesFromState(AutomataState stateSource) {
		if (stateSource.canHaveDotTransition()) {
			
			int destDotIndex = stateSource.getDotIndex() + 1;

			createAutomataState(stateSource, 
					stateSource.getProduction(), 
					destDotIndex, 
					stateSource.getProduction().get(destDotIndex)
					);
			
			if (stateSource.isSymbolAfterDotNonterminal()) {
				createStateFromEpsilon(stateSource);
			}
		}
	}

	private void createStateFromEpsilon(AutomataState stateSource) {
		Symbol symbol = stateSource.getSymbolAfterDot();
		ProductionGroup groupOfSymbol = ProductionGroup.findGroup(normalGroup, symbol.getValue());
		for (Production production : groupOfSymbol) {
			createAutomataState(stateSource, production, 0, Symbol.EPSILON);
		}
	}
	
	private void createAutomataState(
			AutomataState stateSource,
			Production productionSource,
			int newDotIndex,
			Symbol transitionSymbol
			) {
		
		Production production = createDotProduction(productionSource, newDotIndex);
		AutomataState stateDest = searchAutomata(production);
		if (stateDest == null) {
			stateDest = new AutomataState(production, newDotIndex);
			statesAll.add(stateDest);
			createStatesFromState(stateDest);
		}
		stateSource.addTransition(transitionSymbol, stateDest);
	}
	
	private Production createDotProduction(
			Production sourceProduction,
			int dotIndex) {
		
		Production production = new Production();
		for (Symbol symbol : sourceProduction) {
			if (symbol != SymbolDot.DOT) production.add(symbol);
		}
		production.add(dotIndex, SymbolDot.DOT);
		production.setGroup(sourceProduction.getGroup());
		production.setName(sourceProduction.getName());
		return production;
	}

	private AutomataState searchAutomata(Production production) {
		for (AutomataState state : statesAll) {
			if (state.getProduction().equals(production)) {
				return state;
			}
		}
		return null;
	}
	
}
