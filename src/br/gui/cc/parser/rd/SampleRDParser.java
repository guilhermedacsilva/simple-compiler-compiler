package br.gui.cc.parser.rd;

import java.util.List;

import br.gui.cc.lexer.Token;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.Parser;

/**
 * DO NOT USE IT. ITS JUST AN EXAMPLE OF PARSER
 * 
 * Recursive Descent Parser
 * It is not supported: 1+1 > error
 * E -> T | T + T | T - T
 * T -> int * T | int / T | int
 * 
 * Supported
 * X -> ( X ) | epsilon
 */
public class SampleRDParser implements Parser {
	private List<Token> tokenList;
	private int index;

	@Override
	public boolean processTokens(List<Token> tokens) {
		tokenList = tokens;
		index = 0;
		
//		if (!productionE()) {
//			return false;
//		}
		
		if (!productionX()) {
			return false;
		}
		
		return isEnd();
	}
	
	private Token getActual() {
		return tokenList.get(index);
	}
	
	private boolean isEnd() {
		return index >= tokenList.size();
	}

	private boolean consumeEpsilon() {
		return true;
	}
	
	private boolean consumeTerminal(String terminal) {
		if (isEnd()) {
			return false;
		}
		if (getActual().getTokenClass().equals(terminal)) {
			index++;
			return true;
		}
		return false;
	}
	
	/*
	 * X -> ( X ) | epsilon
	 */
	private boolean productionX() {
		int backup = index;
		
		if (productionX1()) return true;
		else index = backup;
		if (productionX2()) return true;
		else index = backup;
		
		return false;
	}
	
	private boolean productionX1() {
		return consumeTerminal("(") && productionX() && consumeTerminal(")");
	}
	
	private boolean productionX2() {
		return consumeEpsilon();
	}
	
	
	private boolean productionE() {
		int backup = index;
		
		if (productionE1()) return true;
		else index = backup;
		if (productionE2()) return true;
		else index = backup;
		if (productionE3()) return true;
		else index = backup;
		
		return false;
	}
	
	private boolean productionE1() {
		return productionT();
	}
	
	private boolean productionE2() {
		return productionT() && consumeTerminal("+") && productionT();
	}
	
	private boolean productionE3() {
		return productionT() && consumeTerminal("-") && productionT();
	}
	
	private boolean productionT() {
		int backup = index;
		
		if (productionT1()) return true;
		else index = backup;
		if (productionT2()) return true;
		else index = backup;
		if (productionT3()) return true;
		else index = backup;
		
		return false;
	}
	
	private boolean productionT1() {
		return consumeTerminal("int");
	}
	
	private boolean productionT2() {
		return consumeTerminal("int") && consumeTerminal("*") && productionT();
	}
	
	private boolean productionT3() {
		return consumeTerminal("int") && consumeTerminal("/") && productionT();
	}
	
	/*
	 * E -> T | T + T | T - T
	 * T -> int | int * T | int / T
	*/
	
	@Override
	public ASTNode getAbstractSintaxTree() {
		return null;
	}
	
}
