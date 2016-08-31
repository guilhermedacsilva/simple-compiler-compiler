package br.gui.cc.all;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gui.cc.lexer.Token;
import br.gui.cc.parser.TestAST;
import br.gui.cc.parser.TestProductionGroup;
import br.gui.cc.semantic.SymbolTableVariable;

public class TestDynamicCompiler {
	public static final String PARAM_PRINT_ALL = "printAll";
	public static final String PARAM_PRINT_TOKEN_LIST = "printTokenList";
	public static final String PARAM_PRINT_PRODUCTION_GROUPS = "printProductionGroups";
	public static final String PARAM_PRINT_AST = "printAST";
	public static final String PARAM_PRINT_CODE = "printCode";
	public static final String PARAM_ASSERT_SYMBOL_TABLE_VARS = "assertSymbolTableSize";
	public static final String PARAM_ASSERT_EXPECTED_CODE = "assertExpectedCode";
	private DynamicCompiler compiler;
	private Map<String, Object> params;
	
	public TestDynamicCompiler(DynamicCompiler compiler) {
		this.compiler = compiler;
		params = new HashMap<String, Object>();
	}

	public void setParam(String name, Object value) {
		params.put(name, value);
	}

	public void run(String program) throws IOException {
		run(program, 4);
	}

	public void run(String program, int level) throws IOException {
		compiler.runLexer(program);
		printTokenList();
		if (level == 1) return;
		compiler.runParser();
		printProductionGroups();
		assertParserOK();
		if (level == 2) return;
		compiler.runSemantic();
		printAST();
		assertVarsQuantity();
		if (level == 3) return;
		compiler.runGenerator();
		printCode();
		assertCode();
	}
	
	private void printTokenList() {
		if (isParamTrue(PARAM_PRINT_ALL) || isParamTrue(PARAM_PRINT_TOKEN_LIST)) {
			List<Token> tokenList = compiler.getTokenList();
			System.out.println("Token list:");
			for (Token token : tokenList) {
				System.out.println("  "+token);
			}
		}
	}
	
	private void printProductionGroups() {
		if (isParamTrue(PARAM_PRINT_ALL) || isParamTrue(PARAM_PRINT_PRODUCTION_GROUPS)) {
			TestProductionGroup.print(compiler.getProductionGroups());
		}
	}
	
	private void assertParserOK() {
		assertTrue(compiler.isParserOK());
	}
	
	private void printAST() {
		if (isParamTrue(PARAM_PRINT_ALL) || isParamTrue(PARAM_PRINT_AST)) {
			TestAST.print(compiler.getParser().getAbstractSintaxTree(), true);
		}
	}
	
	private void assertVarsQuantity() {
		if (params.containsKey(PARAM_ASSERT_SYMBOL_TABLE_VARS)) {
			SymbolTableVariable var;
			final int size = compiler.getSymbolTable().getVarList().size();
			final String[] names = (String[]) params.get(PARAM_ASSERT_SYMBOL_TABLE_VARS);
			for (int index = 0; index < size; index++) {
				var = compiler.getSymbolTable().getVarList().get(index);
				assertEquals(names[index], var.getLexeme());
			}
		}
	}
	
	private void printCode() {
		if (isParamTrue(PARAM_PRINT_ALL) || isParamTrue(PARAM_PRINT_CODE)) {
			System.out.print(compiler.getGeneratedCode());
		}
	}
	
	private void assertCode() {
		if (params.containsKey(PARAM_ASSERT_EXPECTED_CODE)) {
			final String code = compiler.getGeneratedCode();
			String expected = (String) params.get(PARAM_ASSERT_EXPECTED_CODE);
			assertEquals(expected.replaceAll("\r", "").trim(), code.trim());
		}
	}
	
	private boolean isParamTrue(String name) {
		return params.containsKey(name) && params.get(name) == Boolean.TRUE;
	}
	
}
