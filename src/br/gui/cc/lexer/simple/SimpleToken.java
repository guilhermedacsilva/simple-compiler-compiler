package br.gui.cc.lexer.simple;

import br.gui.cc.lexer.Token;

public class SimpleToken implements Token {
	public static final String ERROR = "ERROR";
	public static final String EOF = "EOF";
	
	private String lexeme;
	private String tokenClass;
	private int lineNumber;
	
	public SimpleToken(String lexeme,
			String tokenClass,
			int lineNumber) {
		
		this.lexeme = lexeme;
		this.tokenClass = tokenClass;
		this.lineNumber = lineNumber;
	}
	
	public static SimpleToken createEOF() {
		return new SimpleToken("", EOF, 0);
	}
	
	public String getLexeme() {
		return lexeme;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public String getTokenClass() {
		return tokenClass;
	}
	public boolean isEOF() {
		return EOF.equals(tokenClass);
	}
	public boolean isError() {
		return ERROR.equals(tokenClass);
	}
	@Override
	public String toString() {
		return tokenClass+":"+lexeme;
	}

}
