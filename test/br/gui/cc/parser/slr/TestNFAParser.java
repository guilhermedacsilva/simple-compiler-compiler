package br.gui.cc.parser.slr;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.TestProductionGroup;

public class TestNFAParser {
	private AutomataState state;
	private List<AutomataState> states;
	
	@Test
	public void test() {
		/**
		 * S > E
		 * E > T + E | T
		 * T > int * T | int | (E)
		 */

		ProductionGroup groupS = 
				TestProductionGroup.createGroup("S", Symbol.nonTerminal("E"), null, null, null);
		ProductionGroup groupE =
				TestProductionGroup.createGroup("E", 
						Symbol.nonTerminal("T"), 
						Symbol.terminal("+"), 
						Symbol.nonTerminal("E"), null);
		ProductionGroup groupT =
				TestProductionGroup.createGroup("T", 
						Symbol.terminal("int"), 
						Symbol.terminal("*"), 
						Symbol.nonTerminal("T"), null);
		groupE.add(TestProductionGroup.createProduction(
				Symbol.nonTerminal("T"), null, null, null));
		groupT.add(TestProductionGroup.createProduction(
				Symbol.terminal("int"), null, null, null));
		groupT.add(TestProductionGroup.createProduction(
				Symbol.terminal("("), 
				Symbol.nonTerminal("E"), 
				Symbol.terminal(")"), null));
		
		List<ProductionGroup> groupList = new ArrayList<ProductionGroup>(3);
		groupList.add(groupS);
		groupList.add(groupE);
		groupList.add(groupT);
		
		NFAParser nfa = new NFAParser(groupList);
		nfa.create();
		state = nfa.getAutomata();
		
		assertState(groupS.get(0), 0, 2, Symbol.nonTerminal("E")); // S > .E
		doTransition(Symbol.nonTerminal("E"), 1, 0);
		assertState(groupS.get(0), 1, 0, null); // S > E.
		state = nfa.getAutomata();
		doTransition(Symbol.EPSILON, 2, 0);
		assertState(groupE.get(0), 0, 3, Symbol.nonTerminal("T")); // E > .T + E
		doTransition(Symbol.nonTerminal("T"), 1, 0);
		assertState(groupE.get(0), 1, 0, Symbol.terminal("+")); // E > T . + E
		doTransition(Symbol.terminal("+"), 1, 0);
		assertState(groupE.get(0), 2, 2, Symbol.nonTerminal("E")); // E > T + .E
		doTransition(Symbol.nonTerminal("E"), 1, 0);
		assertState(groupE.get(0), 3, 0, null); // E > T + E .
		state = nfa.getAutomata();
		doTransition(Symbol.EPSILON, 2, 1);
		assertState(groupE.get(1), 0, 3, Symbol.nonTerminal("T")); // E > .T
		doTransition(Symbol.nonTerminal("T"), 1, 0);
		assertState(groupE.get(1), 1, 0, null); // E > T .
		state = nfa.getAutomata();
		doTransition(Symbol.EPSILON, 2, 1);
		assertState(groupE.get(1), 0, 3, Symbol.nonTerminal("T")); // E > .T
		doTransition(Symbol.EPSILON, 3, 0);
		assertState(groupT.get(0), 0, 0, Symbol.terminal("int")); // T > . int * T
		doTransition(Symbol.terminal("int"), 1, 0);
		assertState(groupT.get(0), 1, 0, Symbol.terminal("*")); // T > int . * T
		doTransition(Symbol.terminal("*"), 1, 0);
		assertState(groupT.get(0), 2, 3, Symbol.nonTerminal("T")); // T > int * . T
		doTransition(Symbol.nonTerminal("T"), 1, 0);
		assertState(groupT.get(0), 3, 0, null); // T > int * T .
		state = nfa.getAutomata();
		doTransition(Symbol.EPSILON, 2, 1);
		assertState(groupE.get(1), 0, 3, Symbol.nonTerminal("T")); // E > .T
		doTransition(Symbol.EPSILON, 3, 2);
		assertState(groupT.get(2), 0, 0, Symbol.terminal("(")); // T > .(E)
		doTransition(Symbol.terminal("("), 1, 0);
		assertState(groupT.get(2), 1, 2, Symbol.nonTerminal("E")); // T > (.E)
		doTransition(Symbol.EPSILON, 2, 0);
		assertState(groupE.get(0), 0, 3, Symbol.nonTerminal("T")); // E > . T + E
		
	}
	
	private Production getProdDot(Production p, int dotIndex) {
		Production newProd = new Production();
		for (Symbol s : p) {
			newProd.add(s);
		}
		newProd.add(dotIndex, SymbolDot.DOT);
		return newProd;
	}
	
	private void assertState(
			Production pExpected, 
			int dotIndex,
			int epsilonsExpected, 
			Symbol transitionSymbol) {
		
		assertEquals(getProdDot(pExpected, dotIndex), state.getProduction());

		int epsilonCounter = 0;
		for (Transition t : state.getTransitionList()) {
			if (t.symbol.isEpsilon()) {
				epsilonCounter++;
			} else if (!t.symbol.equals(transitionSymbol)) {
				fail("Transition symbol not found: " + transitionSymbol
						+ "\nIn state: " + state);
			}
		}
		if (epsilonCounter != epsilonsExpected) {
			fail("Epsilons expected: " + epsilonsExpected
					+ ". Epsilons found: " + epsilonCounter
					+ "\nIn state: " + state);
		}
	}

	private void doTransition(Symbol nonTerminal, int sizeExpected, int index) {
		states = state.doTransition(nonTerminal);
		assertEquals(sizeExpected, states.size());
		state = states.get(index);
	}

}
