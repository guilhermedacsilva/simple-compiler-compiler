package br.gui.cc.parser.ll;

import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.Symbol;

public class LL1StackItem {
	public final Symbol symbol;
	public final ASTNode node;
	
	public LL1StackItem(Symbol symbol, ASTNode node) {
		this.symbol = symbol;
		this.node = node;
	}

}