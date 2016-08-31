package br.gui.cc.lexer.simple;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.util.ReaderUtil;

public class TestSimpleLexer {
	private SimpleLexer lexer;
	
	private void setSimpleLexer(Reader builderConfiguration) throws IOException {		
		lexer = (SimpleLexer) SimpleLexerBuilder.initLexer(
					new SimpleLexer(), 
					builderConfiguration);
	}

	private void assertToken(Token token, String lexeme, String tokenClass,
			int lineNumber) {
		
		assertEquals(lexeme, token.getLexeme());
		assertEquals(tokenClass, token.getTokenClass());
		assertEquals(lineNumber, token.getLineNumber());
	}

	@Test
	public void test1() throws Exception {
		setSimpleLexer(new FileReader("test_resources/lexer_3"));
		
		assertEquals(30, lexer.configList.size());
		assertEquals("keyword", lexer.configList.get(0).getClassName());
		assertEquals("dot", lexer.configList.get(10).getClassName());
		assertEquals("@comment", lexer.configList.get(29).getClassName());
		
		lexer.setSourceCode(new FileReader("test_resources/lexer_program_1"));
		assertToken(lexer.getNextToken(), "1", "integer", 1);
		assertToken(lexer.getNextToken(), "nome2", "identifier", 1);
		assertToken(lexer.getNextToken(), "+", "plus", 1);
		assertToken(lexer.getNextToken(), "-1", "integer", 1);
		assertToken(lexer.getNextToken(), "-", "minus", 1);
		assertToken(lexer.getNextToken(), "*", "mult", 1);
		assertToken(lexer.getNextToken(), "/", "div", 1);
		assertToken(lexer.getNextToken(), ";", "semicolon", 1);
		assertToken(lexer.getNextToken(), ".", "dot", 1);
		assertToken(lexer.getNextToken(), "=", "equal", 1);
		assertToken(lexer.getNextToken(), "<-", "attribution", 1);
		assertToken(lexer.getNextToken(), ":", "colon", 1);
		assertToken(lexer.getNextToken(), "true", "bool", 2);
		assertToken(lexer.getNextToken(), ",", "comma", 2);
		assertToken(lexer.getNextToken(), "<", "less", 2);
		assertToken(lexer.getNextToken(), "<=", "less_equal", 2);
		assertToken(lexer.getNextToken(), "<>", "different", 2);
		assertToken(lexer.getNextToken(), ">", "greater", 2);
		assertToken(lexer.getNextToken(), ">=", "greater_equal", 2);
		assertToken(lexer.getNextToken(), "=>", "implies", 2);
		assertToken(lexer.getNextToken(), "{", "block_open", 2);
		assertToken(lexer.getNextToken(), "false", "bool", 3);
		assertToken(lexer.getNextToken(), "(", "parentheses_open", 3);
		// comments are not returned by the lexer 
		// assertToken(lexer.getNextToken(), "(*abc*)", "comment", 3);
		assertToken(lexer.getNextToken(), "@", "at", 3);
		assertToken(lexer.getNextToken(), "~", "negation", 3);
		assertToken(lexer.getNextToken(), ")", "parentheses_close", 3);
		assertToken(lexer.getNextToken(), "}", "block_close", 3);
		assertToken(lexer.getNextToken(), "2.3", "float", 3);
		assertToken(lexer.getNextToken(), "4", "integer", 3);
		assertToken(lexer.getNextToken(), "\"23\"", "string", 3);
		assertToken(lexer.getNextToken(), "fi", "keyword", 3);
	}

	@Test
	public void test2() throws Exception {
		setSimpleLexer(new FileReader("test_resources/lexer_3"));
		
		assertEquals(30, lexer.configList.size());

		lexer.setSourceCode(new FileReader("test_resources/lexer_program_2"));
		assertToken(lexer.getNextToken(), "class", "keyword", 1);
		assertToken(lexer.getNextToken(), "class", "keyword", 2);
		assertToken(lexer.getNextToken(), "classe", "identifier", 3);
		assertToken(lexer.getNextToken(), "clas", "identifier", 4);
	}

	@Test
	public void test3() throws Exception {
		setSimpleLexer(new FileReader("test_resources/lexer_2"));
		lexer.setSourceCode(new StringReader("a"));
		assertToken(lexer.getNextToken(), "a\n", SimpleToken.ERROR, 1);
	}

	@Test
	public void test4() throws Exception {
		setSimpleLexer(new FileReader("test_resources/lexer_2"));
		lexer.setSourceCode(new StringReader(""));
		assertToken(lexer.getNextToken(), "", SimpleToken.EOF, 0);
	}

}
