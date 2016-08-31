package br.gui.cc.parser.rd;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.rd.SampleRDParser;

public class TestSampleRDParser {

	@Test
	public void test1() {
		SampleRDParser parser = new SampleRDParser();
		
		List<Token> tokens = new ArrayList<Token>(10);
		tokens.add(new SimpleToken("(", "(", 0));
		tokens.add(new SimpleToken(")", ")", 0));
		
		assertTrue(parser.processTokens(tokens));
	}

}
