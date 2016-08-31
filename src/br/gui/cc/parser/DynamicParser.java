package br.gui.cc.parser;

import java.util.List;

public interface DynamicParser extends Parser {
	
	/**
	 * Loads a Context Free Grammar
	 * Example: E -> ( E ) | epsilon
	 * 
	 * 
	 * groups = [{
	 * 	name = "E",
	 * 	productions = [
	 * 		[symbol = "(", symbol = "E", symbol = ")"],
	 * 		[symbol = epsilon]
	 * 	]
	 * }]
	 * 
	 */
	void loadCFG(List<ProductionGroup> groups);
	
}
