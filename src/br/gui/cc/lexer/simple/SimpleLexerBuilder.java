package br.gui.cc.lexer.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.TokenConfiguration;

public class SimpleLexerBuilder {
	
	private SimpleLexerBuilder() {}
	
	public static Lexer initLexer(Lexer lexer, Reader config) throws IOException {
		lexer.setConfiguration(getConfigForLexer(config));
		return lexer;
	}

	public static List<TokenConfiguration> getConfigForLexer(Reader reader) throws IOException {
		List<TokenConfiguration> configList = new ArrayList<TokenConfiguration>(20);
		BufferedReader buffer = new BufferedReader(reader);
		String line = "";
		TokenConfiguration config = null;
		boolean lastOneWasClass = false;
		
		while (buffer.ready()) {
			line = buffer.readLine();
			
			if (line == null) break;
			else line = line.trim();
			
			if (shouldIgnore(line)) {
				continue;
				
			} else if (isClass(line)) {
				if (lastOneWasClass) throw new RuntimeException("2 classes in a row: " + line);
				config = new TokenConfiguration(line);
				configList.add(config);
				lastOneWasClass = true;
				
			} else { // pattern
				line = line.replaceAll("\\n", "\n");
				config.getPatternList().add(Pattern.compile(line, Pattern.DOTALL));
				lastOneWasClass = false;
			}
		}
		
		if (lastOneWasClass) throw new RuntimeException("Class without regex: " + line);		
		if (configList.isEmpty()) throw new RuntimeException("No config for lexer.");
		
		return configList;
	}
	
	private static boolean shouldIgnore(String line) {
		return line.isEmpty() || line.startsWith("//");
	}

	private static boolean isClass(String text) {
		return !text.startsWith("^");
	}
	
}
