package br.gui.cc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.gui.cc.gen.TestLang1MathVar;
import br.gui.cc.gen.ex.TestMathGenerator;
import br.gui.cc.gen.ex.TestMathVarGenerator;
import br.gui.cc.lexer.simple.TestSimpleLexer;
import br.gui.cc.lexer.simple.TestSimpleLexerBuilder;
import br.gui.cc.parser.ll.TestLL1Parser;
import br.gui.cc.parser.ll.TestSampleLL1Parser;
import br.gui.cc.parser.rd.TestRecursiveDescentParser;
import br.gui.cc.parser.rd.TestSampleRDParser;
import br.gui.cc.parser.reader.TestCFGReader;
import br.gui.cc.parser.reader.TestECFGReader;
import br.gui.cc.parser.slr.TestNFAParser;
import br.gui.cc.parser.slr.TestSLRParser;
import br.gui.cc.parser.util.TestFirstFollowUtil;
import br.gui.cc.parser.util.TestParsingTable;

@RunWith(Suite.class)
@SuiteClasses({
	// Gen
	TestMathGenerator.class,
	TestMathVarGenerator.class,
	TestLang1MathVar.class,
	// lexer
	TestSimpleLexer.class,
	TestSimpleLexerBuilder.class,
	// parser.ll
	TestLL1Parser.class,
	TestSampleLL1Parser.class,
	// parser.rd
	TestSampleRDParser.class,
	TestRecursiveDescentParser.class,
	// parser.reader
	TestCFGReader.class,
	TestECFGReader.class,
	// parser.slr
	TestNFAParser.class,
	TestSLRParser.class,
	// parser.util
	TestFirstFollowUtil.class,
	TestParsingTable.class
	})
public class TestAllTests {

}
