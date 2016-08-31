package br.gui.cc.gen;

import java.util.HashMap;
import java.util.Map;

import br.gui.cc.parser.ASTNode;
import br.gui.cc.semantic.SymbolTable;

public class SimpleGenerator {
	private static final String INITIAL_CODER_NAME = "0";
	protected Map<String, ICoder> coderMap;
	protected SymbolTable symbolTable;
	protected Map<String, Object> supportMap = new HashMap<String, Object>();

	public void load(Map<String, ICoder> coderMap) {
		this.coderMap = coderMap;
	}

	public String generateAll(ASTNode node, SymbolTable symbolTable) {
		supportMap.clear();
		this.symbolTable = symbolTable;
		return coderMap.get(INITIAL_CODER_NAME).doCode(this, node);
	}

	/**
	 * Default:
	 * 		terminal symbol: ""
	 * 		non-terminal symbol: all children
	 */
	public String generateNode(ASTNode node) {
		if (node.getSymbol().isTerminal()) {
			return generateTerminalSymbol(node);
		}
		return generateNonTerminalSymbol(node);
	}
	
	protected String generateTerminalSymbol(ASTNode node) {
		if (coderMap.get(node.getSymbol().getValue()) != null) {
			return coderMap.get(node.getSymbol().getValue()).doCode(this, node);
		}
		return "";
	}
	
	protected String generateNonTerminalSymbol(ASTNode node) {
		if (coderMap.get(node.getProductionName()) != null) {
			return coderMap.get(node.getProductionName()).doCode(this, node);
		}
		return defaultNonTerminalCode(node);
	}
	
	protected String defaultNonTerminalCode(ASTNode node) {
		String result = "";
		for (ASTNode child : node.getChildren()) {
			result += generateNode(child);
		}
		return result;
	}
	
	public String generateProduction(ASTNode node, String index) {
		if (coderMap.containsKey(index)) {
			return coderMap.get(index).doCode(this, node);
		}
		throw new IllegalArgumentException("CoderMap does not contain key: " + index);
	}
	
	public Map<String, ICoder> getCoderMap() {
		return coderMap;
	}
	
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
	public Map<String, Object> getSupportMap() {
		return supportMap;
	}

}
