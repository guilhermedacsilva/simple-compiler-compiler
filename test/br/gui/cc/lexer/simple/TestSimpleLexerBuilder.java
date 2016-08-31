package br.gui.cc.lexer.simple;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.util.ReaderUtil;

public class TestSimpleLexerBuilder {
	private List<TokenConfiguration> tokenConfigList;
	
	private void setTokenConfigList(String filename) throws Exception {
		tokenConfigList = SimpleLexerBuilder.getConfigForLexer(new FileReader(filename));
	}

	private TokenConfiguration getConfig(int index) {
		return tokenConfigList.get(index);
	}

	private Pattern getPattern(int lIndex, int pIndex) {
		return getConfig(lIndex).getPatternList().get(pIndex);
	}
	
	private boolean match(int lIndex, int pIndex, String text) {
		return getPattern(lIndex, pIndex).matcher(text).matches();
	}

	@Test
	public void test1() throws Exception {
		setTokenConfigList("test_resources/lexer_1");
		
		assertEquals(1, tokenConfigList.size());
		assertEquals("classe", tokenConfigList.get(0).getClassName());
	}

	@Test
	public void test2() throws Exception {
		setTokenConfigList("test_resources/lexer_2");
		
		assertEquals(3, tokenConfigList.size());
		assertEquals("integer", getConfig(0).getClassName());
		assertEquals("float", getConfig(1).getClassName());
		assertEquals("@comment", getConfig(2).getClassName());

		assertEquals(1, getConfig(0).getPatternList().size());
		assertEquals(2, getConfig(1).getPatternList().size());
		assertEquals(1, getConfig(2).getPatternList().size());

		assertTrue(match(0,0, "1"));
		assertTrue(match(1,0, "1."));
		assertTrue(match(1,1, "1.3"));
		assertTrue(match(2,0, "/* hahaha */"));
	}

}
