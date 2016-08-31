package br.gui.cc.parser.rd;

import java.util.List;

import br.gui.cc.lexer.Token;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.DynamicParser;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

public class RecursiveDescentParser implements DynamicParser {
	private List<ProductionGroup> cfg;
	private List<Token> tokenList;
	private int index;
	private ASTNode ast;
	
	@Override
	public void loadCFG(List<ProductionGroup> groups) {
		cfg = groups;
	}
	
	@Override
	public boolean processTokens(List<Token> tokens) {
		tokenList = tokens;
		index = 0;
		
		ast = ASTNode.createRoot(cfg.get(0).getName());
		if (!tryGroup(0, ast)) {
			return false;
		}
		
		return isDone();
	}
	
	private boolean tryGroup(final int g, final ASTNode parent) {
		final int backup = index;
		
		for (Production prod : cfg.get(g)) {
			if (tryProduction(prod, parent)) {
				return true;
			}
			index = backup;
		}
		
		return false;
	}
	
	private boolean tryProduction(Production prod, final ASTNode parent) {		
		ASTNode node;
		for (Symbol symbol : prod) {
			node = new ASTNode(symbol, parent);
			
			if (symbol.isEpsilon()) {
				new ASTNode(Symbol.EPSILON, parent);
				return true;
				
			} else if (symbol.isTerminal()) {
				if (!tryConsumeTerminal(symbol, node)) {
					parent.removeChildren();
					return false;
				}
				
			} else {
				if (!tryGroup(findGroup(symbol), node)) {
					parent.removeChildren();
					return false;
				}
			}
		}
		return true;
	}

	private int findGroup(final Symbol symbol) {
		for (int i = 0; i < cfg.size(); i++) {
			if (cfg.get(i).getName().equals(symbol.getValue())) {
				return i;
			}
		}
		throw new RuntimeException("symbol group not found: " + symbol.getValue());
	}

	private boolean tryConsumeTerminal(final Symbol symbol, final ASTNode node) {
		if (isDone()) return false;
		
		if (symbol.getValue().equals(getToken().getTokenClass())) {
			node.setToken(getToken());
			index++;
			return true;
		}
		return false;
	}

	private Token getToken() {
		return tokenList.get(index);
	}
	
	private boolean isDone() {
		return index >= tokenList.size();
	}
	
	
	@Override
	public ASTNode getAbstractSintaxTree() {
		return ast;
	}
	
}
