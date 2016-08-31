package br.gui.cc.parser.slr;

import br.gui.cc.lexer.Token;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.Symbol;

public class StackItem {
	public final Symbol symbol;
	public final ASTNode node;
	public final Token token;

	public StackItem(Symbol symbol, ASTNode node) {
		this.symbol = symbol;
		this.node = node;
		token = null;
	}
	public StackItem(Symbol symbol, ASTNode node, Token token) {
		this.symbol = symbol;
		this.node = node;
		this.token = token;
	}
	@Override
	public String toString() {
		String msg = "StackItem[symbol="+symbol.getValue();
		if (node != null) msg += ",node="+node.getProductionName();
		if (token != null) msg += ",token="+token.getLexeme();
		return msg + "]";
	}
	
}
