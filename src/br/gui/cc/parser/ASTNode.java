package br.gui.cc.parser;

import java.util.LinkedList;
import java.util.List;

import br.gui.cc.lexer.Token;

public class ASTNode {
	private LinkedList<ASTNode> children;
	private Symbol symbol;
	private Token token;
	private ASTNode parent;
	private String productionName;
	private String type;
	
	public ASTNode(Symbol symbol, ASTNode parent) {
		children = new LinkedList<ASTNode>();
		this.symbol = symbol;
		if (parent != null) setParent(parent);
	}
	
	public void setParent(ASTNode parent) {
		this.parent = parent;
		parent.insertChild(this);
	}
	
	public void setToken(Token token) {
		this.token = token;
	}
	
	public Token getToken() {
		return token;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public List<ASTNode> getChildren() {
		return children;
	}
	
	public static ASTNode createRoot(String value) {
		return new ASTNode(Symbol.nonTerminal(value), null);
	}
	
	private void insertChild(ASTNode node) {
		if (symbol.isTerminal()) {
			throw new RuntimeException("Cannot insert child in terminal node");
		}
		children.add(node);
	}
	
	public void removeChildren() {
		for (ASTNode node : children) {
			node.removeChildren();
		}
		children.clear();
	}
	
	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	public ASTNode getFirstChild() {
		return children.getFirst();
	}
	
	public ASTNode getParent() {
		return parent;
	}
	
	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}
	
	public String getProductionName() {
		return productionName;
	}
	
}
