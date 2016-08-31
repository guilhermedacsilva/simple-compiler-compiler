package br.gui.cc.parser.ll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.DynamicParser;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.util.ParsingTable;

/**
 * Left-to-right leftmost predictive parser
 * Lookahead = 1
 */
public class LL1Parser implements DynamicParser {
	private Map<String, Map<String, Production>> parsingTable;
	private Symbol startSymbol;
	private Stack<LL1StackItem> stack;
	private List<Token> tokenList;
	private int tokenIndex;
	private ASTNode ast;

	@Override
	public void loadCFG(List<ProductionGroup> groups) {
		startSymbol = Symbol.nonTerminal(groups.get(0).getName());
		parsingTable = new ParsingTable(groups).getTable();
	}
	
	@Override
	public boolean processTokens(List<Token> tokens) {
		if (parsingTable == null) throw new RuntimeException(
				"Symbol table does not exist.\nTry call loadCFG(groups).");
		
		tokenList = new ArrayList<Token>(tokens.size()+1);
		tokenList.addAll(tokens);
		tokenList.add(new SimpleToken("", "$", 0));
		
		ast = ASTNode.createRoot(startSymbol.getValue());
		
		stack = new Stack<LL1StackItem>();
		stack.push(new LL1StackItem(Symbol.END, null));
		stack.push(new LL1StackItem(startSymbol, ast));
		
		
		tokenIndex = 0;
		LL1StackItem item;
		Symbol currentSymbol;
		Production action;
		List<LL1StackItem> itemList = new LinkedList<LL1StackItem>(); 
		
		while (!stack.isEmpty()) {
			item = stack.pop();
			currentSymbol = item.symbol;
			
			if (currentSymbol.isTerminal()) {
				if (!tryComsume(currentSymbol)) {
					return false;
				}
				
			} else {
				action = parsingTable.get(currentSymbol.getValue()).get(getTokenClass());
				if (action == null) return false;
				if (isEpsilon(action)) {
					new ASTNode(Symbol.EPSILON, item.node);
					continue;
				}
				
				for (Symbol astSymbol : action) {
					itemList.add(new LL1StackItem(astSymbol, new ASTNode(astSymbol, item.node)));
				}
				Collections.reverse(itemList);
				stack.addAll(itemList);
				itemList.clear();
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
	
	private boolean isEpsilon(Production production) {
		return production.get(0).isEpsilon();
	}

	@Override
	public ASTNode getAbstractSintaxTree() {
		return ast;
	}
	
	private boolean isDone() {
		return tokenIndex >= tokenList.size();
	}
	
	private String getTokenClass() {
		return tokenList.get(tokenIndex).getTokenClass();
	}
	
}
