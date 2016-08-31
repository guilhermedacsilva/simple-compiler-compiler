package br.gui.cc.parser;


public class Symbol {
	protected static final String END_VALUE = "$";
	protected static final String EPSILON_VALUE = "epsilon";
	public static final Symbol EPSILON = terminal(EPSILON_VALUE);
	public static final Symbol END = terminal(END_VALUE);
	private String value;
	private boolean terminal;
	
	protected Symbol(String value, boolean terminal) {
		this.value = value;
		this.terminal = terminal;
	}
	
	public static Symbol terminal(String value) {
		return new Symbol(value, true);
	}
	
	public static Symbol nonTerminal(String value) {
		return new Symbol(value, false);
	}

	public String getValue() {
		return value;
	}
	
	public boolean isTerminal() {
		return terminal;
	}
	
	public void setTerminal(boolean terminal) {
		if (!isEpsilon() && !isEnd()) this.terminal = terminal;
	}
	
	public boolean isEpsilon() {
		return this == EPSILON;
	}
	
	public boolean isEnd() {
		return this == END;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (terminal ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Symbol other = (Symbol) obj;
		if (isEpsilon() && !other.isEpsilon())
			return false;
		if (isEnd() && !other.isEnd())
			return false;
		if (terminal != other.terminal)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return value;
	}

}
