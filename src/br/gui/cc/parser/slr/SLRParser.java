package br.gui.cc.parser.slr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ASTNode;
import br.gui.cc.parser.DynamicParser;
import br.gui.cc.parser.Production;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.Symbol;
import br.gui.cc.parser.util.FirstFollowUtil;

/**
 * Simple Left-to-right Rightmost
 * 
 */
public class SLRParser implements DynamicParser {
	private static final String DUMMY_PREFIX = "dummy--"; 
	private List<ProductionGroup> groupList;
	private NFAParser nfa;
	private FirstFollowUtil ffUtil;
	private Stack<StackItem> stack;
	private List<Token> tokenList;
	private int tokenIndex;
	private String dummyGroupName;
	private ASTNode ast;
	
	@Override
	public void loadCFG(List<ProductionGroup> normalGroupList) {
		groupList = new ArrayList<ProductionGroup>(normalGroupList.size()+1);
		groupList.add(createDummyProduction(normalGroupList));
		groupList.addAll(normalGroupList);
		ffUtil = new FirstFollowUtil(groupList);
		nfa = new NFAParser(groupList);
		nfa.create();
	}
	
	private ProductionGroup createDummyProduction(List<ProductionGroup> normalGroupList) {
		Symbol startSymbol = Symbol.nonTerminal(normalGroupList.get(0).getName());
		Production production = new Production();
		production.add(startSymbol);
		dummyGroupName = DUMMY_PREFIX+startSymbol.getValue();
		ProductionGroup group = new ProductionGroup(dummyGroupName);
		group.add(production);
		return group;
	}
	
	@Override
	public boolean processTokens(List<Token> tokens) {		
		stack = new Stack<StackItem>();
		tokenIndex = -1;
		tokenList = new ArrayList<Token>(tokens.size()+1);
		tokenList.addAll(tokens);
		verifyTokenError();
		tokenList.add(SimpleToken.createEOF());
		
		shift();
		
		boolean shift, reduce;
		while (isHandle()) {
			shift = canShift();
			reduce = canReduce();
			if (shift && reduce) shiftReduceConflict();
			
			if (shift) {
				if (noMoreTokens()) return false; 
				shift();
				
			} else if (reduce) {
				reduce();
			
			} else {
				String error = "Parser error: " + getToken() + "\n";
				for (StackItem item : stack) {
					error += item.toString() + "\n";
				}
				throw new RuntimeException(error);
			}
		}
		
		return isProcessOk();
	}
	
	private void verifyTokenError() {
		for (Token token : tokenList) {
			if (token.isError()) 
				throw new RuntimeException("SLRParser: found token error from lexer.\n"
						+ token.getLexeme());
		}
	}
	
	private boolean isProcessOk() {
		return stack.size() == 1 
				&& stack.get(0).symbol.getValue().equals(dummyGroupName)
				&& tokenIndex == tokenList.size()-2;
	}

	private boolean noMoreTokens() {
		return tokenIndex >= tokenList.size() - 1;
	}

	private void shiftReduceConflict() {
		throw new RuntimeException("shift reduce conflict");
	}

	private boolean isHandle() {
		nfa.reset();
		for (StackItem item : stack) {
			if (!nfa.walk(item.symbol)) return false;
		}
		return true;
	}
	
	private boolean canShift() {
		if (noMoreTokens()) return false;
		nfa.saveStates();
		boolean tryShift = nfa.walk(Symbol.terminal(getNextToken().getTokenClass()));
		nfa.restoreStates();
		return tryShift;
	}
	
	private boolean canReduce() {
		int reduceOks = 0;
		for (AutomataState state : nfa.getStatesOn()) {
			if (!state.canHaveDotTransition()
					&& isTokenInFollowSet(state.getProduction().getGroup().getName())) {
				reduceOks++;
			}
		}
		if (reduceOks > 1) throw new RuntimeException("Reduce reduce conflict");
		return reduceOks == 1;
	}

	private boolean isTokenInFollowSet(String nonTerminalName) {
		for (Symbol symbol : ffUtil.getFollowSet(nonTerminalName)) {
			if (getNextToken().isEOF() && symbol.isEnd()) {
				return true;
			}
			if (getNextToken().getTokenClass().equals(symbol.getValue())) {
				return true;
			}
		}
		return false;
	}

	private void shift() {
		tokenIndex++;
		stack.push(new StackItem(Symbol.terminal(getToken().getTokenClass()), null, getToken()));
	}
	
	private void reduce() {
		Production reduceProduction = null;
		for (AutomataState state : nfa.getStatesOn()) {
			if (!state.canHaveDotTransition()) {
				reduceProduction = state.getProduction();
				break;
			}
		}
		if (reduceProduction == null) throw new RuntimeException("Cannot reduce.");
		
		updateStack(reduceProduction);
	}
	
	/**
	 * dotProduction has 1 more symbol (DOT) than normal
	 */
	private void updateStack(Production dotProduction) {
		List<StackItem> itemList = new ArrayList<StackItem>(dotProduction.size()-1);
		for (int i = 0; i < dotProduction.size()-1; i++) {
			itemList.add(stack.pop());
		}
		Collections.reverse(itemList);
		
		Symbol nonTerminal = Symbol.nonTerminal(dotProduction.getGroup().getName());
		
		ASTNode parent = new ASTNode(nonTerminal, null);
		parent.setProductionName(dotProduction.getName());
		
		for (StackItem item : itemList) {
			if (item.node == null) {
				ASTNode leafNode = new ASTNode(item.symbol, parent);
				leafNode.setToken(item.token);
			} else {
				item.node.setParent(parent);
			}
		}
		if (!nonTerminal.getValue().equals(dummyGroupName)) ast = parent;
		
		stack.push(new StackItem(nonTerminal, parent));
	}

	private Token getToken() {
		return tokenList.get(tokenIndex);
	}
	
	private Token getNextToken() {
		return tokenList.get(tokenIndex+1);
	}
	
	@Override
	public ASTNode getAbstractSintaxTree() {
		return ast;
	}
}
