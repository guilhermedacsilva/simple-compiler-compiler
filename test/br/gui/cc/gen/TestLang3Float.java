package br.gui.cc.gen;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.gui.cc.all.DynamicCompiler;
import br.gui.cc.all.DynamicCompilerBuilder;
import br.gui.cc.all.TestDynamicCompiler;
import br.gui.cc.all.TestDynamicCompilerShortcut;
import br.gui.cc.gen.ex.MathVarGenerator;
import br.gui.cc.gen.reader.GeneratorReader;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.lexer.simple.SimpleToken;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;
import br.gui.cc.parser.reader.ECFGReader;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.parser.slr.TestSLRParser;
import br.gui.cc.semantic.SimpleSemantic;
import br.gui.cc.semantic.SymbolTable;
import br.gui.cc.semantic.SymbolTableReader;
import br.gui.cc.semantic.TypeReader;

public class TestLang3Float {
	private static final String FOLDER = "test_resources/lang_3_float/";
	private static DynamicCompiler compiler;

	@BeforeClass
	public static void createCompiler() {
		DynamicCompilerBuilder builder = new DynamicCompilerBuilder();
		builder.setLexerFile(FOLDER+"lexer")
			.setParserFile(FOLDER+"parser")
			.setSemanticFile(FOLDER+"semantic")
			.setSymbolTableFile(FOLDER+"symbol_table")
			.setGenFile(FOLDER+"gen");
		compiler = builder.build();
	}
	
	@Test
	public void testFloat1() throws Exception {
		String programFile = FOLDER+"program_1";
		String[] vars = {"a"};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(TestDynamicCompiler.PARAM_PRINT_ALL, true);
		TestDynamicCompilerShortcut.test(compiler, programFile, vars, params, 3);
	}
}