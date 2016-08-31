package br.gui.cc.semantic;

import java.util.Map;

import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.Symbol;

/**
 * must check
 * ids are declared
 * types
 * inheritance
 * class/methods defined only once
 * reserved ids are not misused
 */
public class SimpleSemantic {
	public static final String CONFLICT_TYPE = "@conflict";
	private ASTNode astNode;
	private Map<String, Object> semanticMap;
	private TypeConflict typeConflict;
	
	public SimpleSemantic(ASTNode astNode) {
		this.astNode = astNode;
	}
	
	public void setConfiguration(Map<String, Object> semanticMap) {
		this.semanticMap = semanticMap; 
		typeConflict = (TypeConflict) semanticMap.get(CONFLICT_TYPE);
	}

	public boolean check() {
		return check(false);
	}

	public boolean check(boolean throwException) {
		if (throwException) {
			checkType(astNode);
			return true;
		
		} else {
			try {
				checkType(astNode);
				return true;
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return false;
	}

	private String checkType(ASTNode node) {
		String typeFromChildren = checkChildrenType(node);
		String typeFromMap = getTypeFromMap(node.getSymbol());
		final String nodeType = typeFromMap != null ? typeFromMap : typeFromChildren; 
		node.setType(nodeType);
		updateChildrenType(node, nodeType);
		return nodeType;
	}
	
	private String checkChildrenType(ASTNode node) {
		String typeFromChildren = null;
		String tempType = null;
		for (ASTNode child : node.getChildren()) {
			tempType = checkType(child);
			if (typeFromChildren == null && tempType != null) {
				typeFromChildren = tempType;
			} else if (typeFromChildren != null && tempType != null && !typeFromChildren.equals(tempType)) {
				typeFromChildren = typeConflict.resolve(typeFromChildren, tempType);
			}
		}
		return typeFromChildren;
	}
	
	private void updateChildrenType(ASTNode node, String type) {
		for (ASTNode child : node.getChildren()) {
			if (child.getType() == null) {
				child.setType(type);
			}
		}
	}

	/**
	 * type null means anything
	 */
	private String getTypeFromMap(Symbol symbol) {
		if (semanticMap.containsKey(symbol.getValue())) {
			return (String)semanticMap.get(symbol.getValue());
		}
		return null;
	}
	
}
