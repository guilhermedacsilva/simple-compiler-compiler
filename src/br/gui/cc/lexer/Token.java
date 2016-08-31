package br.gui.cc.lexer;

public interface Token {

	String getLexeme();
	
	int getLineNumber() ;
	
	String getTokenClass();
	
	boolean isEOF();
	
	boolean isError();
	
}
