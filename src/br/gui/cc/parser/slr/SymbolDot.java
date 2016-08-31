package br.gui.cc.parser.slr;

import br.gui.cc.parser.Symbol;

public class SymbolDot extends Symbol {
	public static final Symbol DOT = terminal("DOT");

	private SymbolDot(String value, boolean terminal) {
		super(value, terminal);
	}
	
}
