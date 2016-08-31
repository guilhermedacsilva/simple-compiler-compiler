package br.gui.cc.parser.slr;

import java.util.LinkedList;
import java.util.List;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.Symbol;

public class AutomataState {
	private static int ID_GENERATOR = 0;
	private final int id;
	private List<Transition> transitionList;
	private Production production;
	private int dotIndex;
	
	public AutomataState(Production production, int dotIndex) {
		this.production = production;
		this.dotIndex = dotIndex;
		transitionList = new LinkedList<Transition>();
		id = ID_GENERATOR++;
	}
	
	public int getId() {
		return id;
	}
	
	public Production getProduction() {
		return production;
	}
	
	public int getDotIndex() {
		return dotIndex;
	}
	
	public boolean canHaveDotTransition() {
		return dotIndex+1 < production.size();
	}
	
	public void addTransition(Symbol symbol, AutomataState state) {
		transitionList.add(new Transition(symbol, state));
	}
	
	public List<Transition> getTransitionList() {
		return transitionList;
	}
	
	public List<AutomataState> doTransition(Symbol s) {
		List<AutomataState> stateDest = new LinkedList<AutomataState>();
		for (Transition t : transitionList) {
			if (t.symbol.equals(s)) {
				stateDest.add(t.state);
			}
		}
		return stateDest;
	}

	public boolean isSymbolAfterDotNonterminal() {
		if (!canHaveDotTransition()) return false;
		return !production.get(dotIndex+1).isTerminal();
	}
	
	public Symbol getSymbolAfterDot() {
		if (!canHaveDotTransition()) throw new RuntimeException("There is not a symbol after dot");
		return production.get(dotIndex+1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dotIndex;
		result = prime * result
				+ ((production == null) ? 0 : production.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutomataState other = (AutomataState) obj;
		if (dotIndex != other.dotIndex)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String msg = "State: " + production + "\nTransitions: \n";
		for (Transition t : transitionList) {
			msg += "-- " + t + "\n";
		}
		return msg;
	}
	
}
