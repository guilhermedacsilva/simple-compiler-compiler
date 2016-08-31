package br.gui.cc.lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface Lexer {

	/**
	 * tokens of the language
	 */
	void setConfiguration(List<TokenConfiguration> configList);
	
	/**
	 * source code
	 */
	void setSourceCode(Reader reader) throws IOException;
	
	/**
	 * if EOF, it must return a Token with isEOF = true
	 * if occurs a lexical error, it must return a Token with isError = true
	 */
	Token getNextToken();
	
	List<Token> getAllTokens();
	
}
