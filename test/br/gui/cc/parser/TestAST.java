package br.gui.cc.parser;

import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.Queue;

import br.gui.cc.parser.ASTNode;

public class TestAST {

	public static void print(ASTNode node, boolean verbose) {
		print(node, verbose, 0);
	}
	
	private static void print(ASTNode node, boolean verbose, int indent) {
		printIndent(indent);
		if (verbose) {
			System.out.print(node.getSymbol());
			if (node.getToken() != null) System.out.print(" " + node.getToken());
			if (!node.getSymbol().isTerminal()) System.out.print(" (lex="+node.getProductionName()+")");
			System.out.print(" (typ="+node.getType()+")");
			System.out.print("\n");
		}
		else System.out.println(node.getSymbol());
		
		for (ASTNode child : node.getChildren()) {
			print(child, verbose, indent+1);
		}
	}
	
	private static void printIndent(int i) {
		while (i-- > 0) System.out.print("  ");
	}
	
	private static class Item {
		ASTNode node;
		int line;
		int column;
		
		public Item(ASTNode node, int line, int column) {
			this.node = node;
			this.line = line;
			this.column = column;
		}
		
	}

	public static void assertAST(ASTNode astExpected, ASTNode astActual) {
		assertEquals(astExpected.getSymbol(), astActual.getSymbol());
		assertEquals(astExpected.getChildren().size(), astActual.getChildren().size());
		for (int i = 0; i < astExpected.getChildren().size(); i++) {
			assertAST(astExpected.getChildren().get(i), astActual.getChildren().get(i));
		}
	}

}
