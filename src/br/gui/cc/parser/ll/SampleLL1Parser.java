package br.gui.cc.parser.ll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.Parser;
import br.gui.cc.parser.Symbol;

/**
 * DO NOT USE IT. ITS JUST AN EXAMPLE OF PARSER
 * --------------------------------------
 * E -> T | T + T 		-- firsts: int | int -- error
 * T -> int | int * T 	-- firsts: int | int -- error
 * 
 * ----- Cannot have 2 productions with the same first terminal
 * ------------------ LEFT REFACTORING
 * 
 * E -> T E' 				-- firsts: int -- ok
 * E' -> +T | epsilon 		-- firsts: + | epsilon -- ok
 * T -> int T' 				-- firsts: int -- ok
 * T' -> * T | epsilon 		-- firsts: * | epsilon -- ok
 * 
 * 		First	Follow
 * E  	int		$
 * E'	+ ep	$
 * T  	int		$ +
 * T'	* ep 	$ +
 * 
 * 		int		+		*		$
 * E	T E'
 * E'			+T				ep
 * T	int T'
 * T'			ep		* T		ep
 */
public class SampleLL1Parser implements Parser {
	private static Map<String, Map<String, List<Symbol>>> symbolTable;
	private Stack<Symbol> stack;
	private List<Token> tokenList;
	private int tokenIndex;
	
	@Override
	public boolean processTokens(List<Token> tokens) {
		tokenList = new ArrayList<Token>(tokens.size()+1);
		tokenList.addAll(tokens);
		tokenList.add(new SimpleToken("", "$", 0));

		stack = new Stack<Symbol>();
		stack.push(Symbol.END);
		stack.push(Symbol.nonTerminal("E"));
		
		tokenIndex = 0;
		int i;
		Symbol currentSymbol;
		List<Symbol> action = null;
		while (!stack.isEmpty()) {
			currentSymbol = stack.pop();
			
			if (currentSymbol.isTerminal()) {
				if (!tryComsume(currentSymbol)) {
					return false;
				}
			} else {
				action = checkSymbolTable(currentSymbol);
				if (action == null) return false;
				
				if (!action.get(0).isEpsilon()) {
					for (i = action.size()-1; i >= 0; i--) {
						stack.push(action.get(i));
					}
				}
			}
		}
		
		return isDone();
	}
	
	private boolean tryComsume(Symbol currentSymbol) {
		if (isDone()) return false;
		
		if (currentSymbol.getValue().equals(getTokenClass())) {
			tokenIndex++;
			return true;
		}
		return false;
	}
	
	private List<Symbol> checkSymbolTable(Symbol currentSymbol) {
		return getSymbolTable().get(currentSymbol.getValue()).get(getTokenClass());
	}

	@Override
	public ASTNode getAbstractSintaxTree() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 		int		+		*		$
	 * E	T E'
	 * E'			+T				ep
	 * T	int T'
	 * T'			ep		* T		ep
	 */
	private static Map<String, Map<String, List<Symbol>>> getSymbolTable() {
		if (symbolTable == null) {
			List<Symbol> cell = new ArrayList<Symbol>(2);
			symbolTable = new HashMap<String, Map<String,List<Symbol>>>();
			
			cell.add(Symbol.nonTerminal("T"));
			cell.add(Symbol.nonTerminal("E'"));
			HashMap<String, List<Symbol>> columns = new HashMap<String, List<Symbol>>();
			columns.put("int", cell);
			symbolTable.put("E", columns);
			
			columns = new HashMap<String, List<Symbol>>();
			cell = new ArrayList<Symbol>(2);
			cell.add(Symbol.terminal("+"));
			cell.add(Symbol.nonTerminal("T"));
			columns.put("+", cell);
			cell = new ArrayList<Symbol>(1);
			cell.add(Symbol.EPSILON);
			columns.put("$", cell);
			symbolTable.put("E'", columns);
			
			columns = new HashMap<String, List<Symbol>>();
			cell = new ArrayList<Symbol>(2);
			cell.add(Symbol.terminal("int"));
			cell.add(Symbol.nonTerminal("T'"));
			columns.put("int", cell);
			symbolTable.put("T", columns);

			columns = new HashMap<String, List<Symbol>>();
			cell = new ArrayList<Symbol>(2);
			cell.add(Symbol.terminal("*"));
			cell.add(Symbol.nonTerminal("T"));
			columns.put("*", cell);
			cell = new ArrayList<Symbol>(1);
			cell.add(Symbol.EPSILON);
			columns.put("+", cell);
			columns.put("$", cell);
			symbolTable.put("T'", columns);
		}
		return symbolTable;
	}
	
	private boolean isDone() {
		return tokenIndex >= tokenList.size();
	}
	
	private String getTokenClass() {
		return tokenList.get(tokenIndex).getTokenClass();
	}
	
}
