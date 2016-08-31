package br.gui.cc.lexer.simple;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.util.ReaderUtil;

public class SimpleLexerBuilderOld {
	
	private SimpleLexerBuilderOld() {}
	
	public static Lexer initLexer(Lexer lexer, Reader config) throws IOException {
		lexer.setConfiguration(getConfigForLexer(config));
		return lexer;
	}
	
	public static List<TokenConfiguration> getConfigForLexer(Reader config) throws IOException {
		final ReaderUtil configReader = new ReaderUtil(config);
		List<TokenConfiguration> lexerConfigList = new ArrayList<TokenConfiguration>(50);
		TokenConfiguration tokenConfig = null;
		String buffer;
		int ruleQuantity;
		boolean dontCheckLastRule;
		
		while (!configReader.isDone()) {
			tokenConfig = new TokenConfiguration(readLine(configReader, true));
			buffer = readLine(configReader, true);
			ruleQuantity = Integer.parseInt(buffer);
			
			for (int i = 0; i < ruleQuantity; i++) {
				dontCheckLastRule = i+1 < ruleQuantity;
				buffer = readLine(configReader, dontCheckLastRule);
				tokenConfig.getPatternList().add(Pattern.compile(buffer));
			}
			lexerConfigList.add(tokenConfig);
		}
		if (lexerConfigList.isEmpty()) {
			throw new RuntimeException("No rules");
		}
		return lexerConfigList;
	}
	
	private static String readLine(ReaderUtil configReader, boolean checkEOF) {
		StringBuilder result = new StringBuilder();
		
		while (!configReader.isDone() && configReader.getCurrentChar() != '\n') {
			if (isNextCharNewLine(configReader)) {
				result.append('\n');
				configReader.walkPosition(2);
				
			} else {		
				result.append(configReader.getCurrentChar());
				configReader.walkPosition();
			}
		}
		configReader.walkPosition();
		if (checkEOF && configReader.isDone()) {
			throw new RuntimeException("Unexpected EOF");
		}
		return result.toString();
	}
	
	private static boolean isNextCharNewLine(ReaderUtil configReader) {
		return configReader.getCurrentChar() == '\\'
				&& !configReader.isDoneOffset(1)
				&& configReader.getNextChar() == 'n';
	}
	
}
