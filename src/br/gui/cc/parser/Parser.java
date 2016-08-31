package br.gui.cc.parser;

import java.util.List;

import br.gui.cc.lexer.Token;

public interface Parser {

	/**
	 * the parser will get the tokens
	 */
	boolean processTokens(List<Token> tokens);
	
	/**
	 * 
	 */
	ASTNode getAbstractSintaxTree();
	
	
}
