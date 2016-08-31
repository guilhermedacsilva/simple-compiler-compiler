package br.gui.cc.lexer.simple;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.util.ReaderUtil;

public class SimpleLexer implements Lexer {
	private static String TOKEN_COMMENT = "@comment";
	private static Boolean concurrency = false;
	protected List<TokenConfiguration> configList;
	private ReaderUtil programReader;
	private int lineNumber;
	private int parcialFound;
	
	public static void setConcurrency(boolean concurrecy) {
		SimpleLexer.concurrency = concurrecy;
	}
	
	public void setConfiguration(List<TokenConfiguration> configList) {
		this.configList = configList;
	}

	public void setSourceCode(Reader reader) throws IOException {
		programReader = new ReaderUtil(reader);
		lineNumber = 1;
	}
	
	public Token getNextToken() {
		Token token = null;
		do {
			token = getNextTokenReal();
		} while (token != null && token.getTokenClass().equals(TOKEN_COMMENT));
		return token;
	}
	
	private SimpleToken getNextTokenReal() {
		while (!programReader.isDone() && programReader.isWhitespace()) { 
			if (programReader.getCurrentChar() == '\n') {
				lineNumber++;
			}
			programReader.walkPosition();
		}
		if (programReader.isDone()) {
			return new SimpleToken("",SimpleToken.EOF, 0);
		}
		return tryReadLexeme();
	}
	
	private SimpleToken tryReadLexeme() {
		final int tokenLine = lineNumber;
		StringBuilder lexeme = new StringBuilder();
		lexeme.append(programReader.getCurrentChar());
		parcialFound = 0;
		TokenConfiguration token = null;
		TokenConfiguration tokenOld = null;
		
		while (true) {
			token = tryApplyRulesPattern(lexeme.toString());

			if (programReader.getCurrentChar() == '\n') lineNumber++;
			programReader.walkPosition();
			
			if (token != null) {
				tokenOld = token;
				if (programReader.isDone()) {
					return new SimpleToken(lexeme.toString(), token.getClassName(), tokenLine);
				}
				token = null;
				
			} else if (tokenOld != null && parcialFound == 0) {
				programReader.walkPosition(-1);
				if (programReader.getCurrentChar() == '\n') lineNumber--;
				lexeme.setLength(lexeme.length()-1);
				return new SimpleToken(lexeme.toString(), tokenOld.getClassName(), tokenLine);
				
			} else if (parcialFound > 0) {
				tokenOld = null;
			}
			
			if (programReader.isDone()) {
				return new SimpleToken(lexeme.toString(), SimpleToken.ERROR, tokenLine);
			}
			
			lexeme.append(programReader.getCurrentChar());
			parcialFound = 0;
			
		}
	}

	private TokenConfiguration tryApplyRulesPattern(String lexeme) {
		if (concurrency) {
			synchronized (concurrency) {
				return applyRulesPattern(lexeme);
			}
		}
		return applyRulesPattern(lexeme);
	}
	
	private TokenConfiguration applyRulesPattern(String lexeme) {
		TokenConfiguration config;
		TokenConfiguration tokenFound = null;
		Pattern pattern;
		for (int configIndex = configList.size()-1; configIndex >= 0; configIndex--) {
			config = configList.get(configIndex);
			for (int i = 0; i < config.getPatternList().size(); i++) {
				pattern = config.getPatternList().get(i);
				if (pattern.matcher(lexeme).matches()){
					if (i+1 == config.getPatternList().size()) {
						tokenFound = config;
					} else {
						parcialFound++;
					}
				}
			}
		}
		return tokenFound;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		SimpleLexer other = new SimpleLexer();
		other.configList = configList;
		return other;
	}

	@Override
	public List<Token> getAllTokens() {
		List<Token> tokenList = new ArrayList<Token>(256);
		Token token = getNextToken();
		while (!token.isEOF() && !token.isError()) {
			tokenList.add(token);
			token = getNextToken();
		}
		if (token.isError()) {
			tokenList.clear();
			tokenList.add(token);
		}
		return tokenList;
	}

}
