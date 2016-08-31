package br.gui.cc.parser.reader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

/**
 * Context-Free Grammar Reader
 */
public class CFGReader {
	protected static final String LEXER_CONFIG_FOR_CLASSES = "class\n^[^\\n ]+$";
	protected List<ProductionGroup> groupList;
	protected Lexer lexer;
	protected HashSet<String> nonTerminals;
	protected List<Symbol> allSymbols;
	
	public CFGReader() {}
	
	public static List<ProductionGroup> processCFG(Reader grammarReader)
			throws IOException {
		CFGReader cfgReader = new CFGReader();
		cfgReader.process(grammarReader);
		return cfgReader.getProductionGroups();
	}
	
	public List<ProductionGroup> getProductionGroups() {
		return groupList;
	}
	
	protected void init() {
		nonTerminals = new HashSet<String>();
		allSymbols = new ArrayList<Symbol>();
		groupList = new ArrayList<ProductionGroup>();
	}
	
	public void process(Reader grammarReader) throws IOException {
		init();
		createLexer(grammarReader);
		createGroups();
		verifyNoGroup();
		setTerminalSymbols();
	}
	
	protected void createGroups() {
		Production production = null;
		Symbol symbol = null;
		Token token = lexer.getNextToken();
		
		int lastLine = -1;
		while (!token.isEOF()) {
			if (token.isError()) {
				throw new RuntimeException(
						"Token error.\nLine: " + token.getLineNumber()
						+ "\nLexeme: " + token.getLexeme());
			}
			if (lastLine != token.getLineNumber()) {
				lastLine = token.getLineNumber();
				production = createProduction(token.getLexeme());
			} else {
				symbol = createSymbol(token.getLexeme());
				production.add(symbol);
			}
			token = lexer.getNextToken();
		}
	}
	
	protected Symbol createSymbol(String lexeme) {
		if (lexeme.equals(Symbol.EPSILON.getValue())) {
			return registerSymbol(Symbol.EPSILON);
		} else {
			return registerSymbol(Symbol.terminal(lexeme));
		}
	}
	
	protected Symbol registerSymbol(Symbol symbol) {
		allSymbols.add(symbol);
		return symbol;
	}

	protected void verifyNoGroup() {
		if (groupList.isEmpty()) {
			throw new RuntimeException("ParserBuilder: no tokens found in definitions");
		}
	}
	
	protected void setTerminalSymbols() {
		for (Symbol sym : allSymbols) {
			sym.setTerminal(!nonTerminals.contains(sym.getValue()));
		}
	}
	
	protected void createLexer(Reader grammarReader) throws IOException {
		lexer = SimpleLexerBuilder.initLexer(
				new SimpleLexer(), 
				new StringReader(LEXER_CONFIG_FOR_CLASSES));
		
		lexer.setSourceCode(grammarReader);
	}

	protected Production createProduction(String groupName) {
		ProductionGroup group = tryGetGroup(groupName);
		if (group == null) {
			group = new ProductionGroup(groupName);
			groupList.add(group);
			nonTerminals.add(groupName);
		}
		final String productionName = groupName+group.size();
		Production production = new Production();
		group.add(production);
		production.setName(productionName);
		return production;
	}
	
	protected ProductionGroup tryGetGroup(String name) {
		for (ProductionGroup group : groupList) {
			if (group.getName().equals(name)) {
				return group;
			}
		}
		return null;
	}
	

}
