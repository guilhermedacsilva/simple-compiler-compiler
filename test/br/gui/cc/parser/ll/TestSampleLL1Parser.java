package br.gui.cc.parser.ll;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;

public class TestSampleLL1Parser {

	@Test
	public void test() {
		List<Token> tokens = new ArrayList<Token>(3);
		tokens.add(new SimpleToken("1", "int", 0));
		tokens.add(new SimpleToken("+", "+", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		tokens.add(new SimpleToken("*", "*", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		tokens.add(new SimpleToken("*", "*", 0));
		tokens.add(new SimpleToken("2", "int", 0));
		
		SampleLL1Parser parser = new SampleLL1Parser();
		
		assertTrue(parser.processTokens(tokens));
	}

}
