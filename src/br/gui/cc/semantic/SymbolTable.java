package br.gui.cc.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.gui.cc.parser.ASTNode;

/**
 * FOR NOW: ONLY GLOBALS, NO SCOPE
 */
public class SymbolTable {
	private Map<String, String> symbolMap;
	private List<SymbolTableVariable> varList;
	
	public SymbolTable(Map<String, String> symbolMap) {
		this.symbolMap = symbolMap;
		varList = new ArrayList<SymbolTableVariable>();
	}
	
	public List<SymbolTableVariable> getVarList() {
		return varList;
	}
	
	public void processAST(ASTNode ast) {
		final String symbolValue = ast.getSymbol().getValue();
		if (symbolMap.containsKey(symbolValue)) {
			if (symbolMap.get(symbolValue).equals("global_var")
					&& !contains(ast.getToken().getLexeme())) {
				varList.add(new SymbolTableVariable(ast.getToken().getLexeme()));
			}
		} else {
			for (ASTNode child : ast.getChildren()) {
				processAST(child);
			}
		}
	}
	
	private boolean contains(String lexeme) {
		for (SymbolTableVariable stvar : varList) {
			if (stvar.getLexeme().equals(lexeme)) {
				return true;
			}
		}
		return false;
	}

}
