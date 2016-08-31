package br.gui.cc.semantic;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;

/**
 * Context-Free Grammar Reader
 */
public class TypeReader {
	protected static final String LEXER_CONFIG_FOR_CLASSES = "word\n^[^\\n ]+$";
	
	public TypeReader() {}
	
	public static HashMap<String, Object> createSemanticMap(Reader typeReader)
			throws IOException {
		
		Lexer lexer = SimpleLexerBuilder.initLexer(
				new SimpleLexer(), 
				new StringReader(LEXER_CONFIG_FOR_CLASSES));
		
		lexer.setSourceCode(typeReader);
		
		Token token = lexer.getNextToken();
		HashMap<String, Object> symbolMap = new HashMap<String, Object>();
		TypeConflict typeConflict = new TypeConflict();
		symbolMap.put(SimpleSemantic.CONFLICT_TYPE, typeConflict);
		String type = null;
		boolean isConflict = false;
		
		int lastLine = -1;
		while (!token.isEOF()) {
			if (token.isError()) {
				throw new RuntimeException(
						"Token error.\nLine: " + token.getLineNumber()
						+ "\nLexeme: " + token.getLexeme());
			}
			if (lastLine != token.getLineNumber()) {
				lastLine = token.getLineNumber();
				type = (String) token.getLexeme();
				isConflict = false;
				if (type.equals(SimpleSemantic.CONFLICT_TYPE)) {
					isConflict = true;
					token = lexer.getNextToken();
					if (lastLine != token.getLineNumber()) {
						throw new IllegalArgumentException("malformed conflict rule");
					}
					type = (String) token.getLexeme();
					typeConflict.createRule(type);
				}
			} else {
				// same line
				if (isConflict) {
					typeConflict.addTypeToRule(token.getLexeme());
					
				} else {
					symbolMap.put(token.getLexeme(), type);
				}
			}
			token = lexer.getNextToken();
		}
		return symbolMap;
	}

}
