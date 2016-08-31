package br.gui.cc.parser.reader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;

/**
 * Extended Context-Free Grammar Reader
 * Automatically creates productions for T* T+ and/or T?
 */
public class ECFGReader extends CFGReader {

	public static List<ProductionGroup> processECFG(Reader grammarReader)
			throws IOException {
		ECFGReader cfgReader = new ECFGReader();
		cfgReader.process(grammarReader);
		return cfgReader.getProductionGroups();
	}
	
	@Override
	protected Symbol createSymbol(String lexeme) {
		if (!alreadyCreatedGroup(lexeme)) {
			if (lexeme.endsWith("+")) {
				createPlusGroup(lexeme.substring(0, lexeme.length()-1));
				
			} else if (lexeme.endsWith("?")) {
				createOptionalGroup(lexeme.substring(0, lexeme.length()-1));
				
			} else if (lexeme.endsWith("*")) {
				createStarGroup(lexeme.substring(0, lexeme.length()-1));
			}
		}
		
		return super.createSymbol(lexeme);
	}

	private boolean alreadyCreatedGroup(String lexeme) {
		return nonTerminals.contains(lexeme);
	}
	
	/**
	T+ T
	T+ T T+
	*/
	private void createPlusGroup(String lexeme) {
		Production prod = createProduction(lexeme+"+");
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme)));

		prod = createProduction(lexeme+"+");
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme)));
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme+"+")));		
	}
	
	/**
	T? EPSILON
	T? T
	*/
	private void createOptionalGroup(String lexeme) {
		Production prod = createProduction(lexeme+"?");
		prod.add(registerSymbol(Symbol.EPSILON));

		prod = createProduction(lexeme+"?");
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme)));
	}
	
	/**
	T* EPSILON
	T* T T*
	*/
	private void createStarGroup(String lexeme) {
		Production prod = createProduction(lexeme+"*");
		prod.add(registerSymbol(Symbol.EPSILON));

		prod = createProduction(lexeme+"*");
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme)));
		prod.add(registerSymbol(Symbol.nonTerminal(lexeme+"*")));
	}
	
}
