package br.gui.cc.all;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.gen.reader.GeneratorReader;
import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.parser.DynamicParser;
import br.gui.cc.parser.ProductionGroup;
import br.gui.cc.parser.reader.ECFGReader;
import br.gui.cc.parser.slr.SLRParser;
import br.gui.cc.semantic.SimpleSemantic;
import br.gui.cc.semantic.SymbolTable;
import br.gui.cc.semantic.SymbolTableReader;
import br.gui.cc.semantic.TypeReader;

public class DynamicCompiler {
	public static final String CONFIG_FILE_GEN = "gen";
	public static final String CONFIG_FILE_SEMANTIC = "semantic";
	public static final String CONFIG_FILE_SYMBOL_TABLE = "symbolTable";
	public static final String CONFIG_FILE_PARSER = "parser";
	public static final String CONFIG_FILE_LEXER = "lexer";
	private Map<String, String> configMap;
	// lexer
	private Lexer lexer;
	private List<Token> tokenList;
	// parser
	private List<ProductionGroup> productionGroups;
	private DynamicParser parser;
	private boolean parserOK;
	// semantic
	private SimpleSemantic semanticParser;
	private Map<String, Object> semanticMap;
	private Map<String, String> symbolMap;
	private SymbolTable symbolTable;
	// gen
	private SimpleGenerator generator;
	private Map<String, ICoder> coderMap;
	private String generatedCode;
	
	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}
	
	public void compileProgram(String program) throws IOException {
		runLexer(program);
		runParser();
		runSemantic();
		runGenerator();
	}

	/**
	 * Set generator, coder map and generated code.
	 */
	public void runGenerator() throws IOException {
		generator = new SimpleGenerator();
		GeneratorReader generatorReader = new GeneratorReader();
		coderMap = 
				generatorReader.createCoderMap(new FileReader(configMap.get(CONFIG_FILE_GEN))); 
		generator.load(coderMap);
		generatedCode = generator.generateAll(parser.getAbstractSintaxTree(), symbolTable);
	}

	/**
	 * Set semantic parser, semantic map, symbol map and symbol table.
	 */
	public void runSemantic() throws IOException {
		semanticParser = new SimpleSemantic(parser.getAbstractSintaxTree());
		semanticMap = 
				TypeReader.createSemanticMap(new FileReader(configMap.get(CONFIG_FILE_SEMANTIC)));
		semanticParser.setConfiguration(semanticMap);
		semanticParser.check(true);
		symbolMap = 
				SymbolTableReader.createSymbolMap(new FileReader(configMap.get(CONFIG_FILE_SYMBOL_TABLE)));
		symbolTable = new SymbolTable(symbolMap);
		symbolTable.processAST(parser.getAbstractSintaxTree());
	}

	/**
	 * Set parser and production groups.
	 */
	public void runParser() throws IOException {
		productionGroups = 
				ECFGReader.processECFG(new FileReader(configMap.get(CONFIG_FILE_PARSER)));
		parser = new SLRParser();
		parser.loadCFG(productionGroups);
		parserOK = parser.processTokens(tokenList);
	}

	/**
	 * Set lexer and token list.
	 */
	public void runLexer(String program) throws IOException {
		lexer = (SimpleLexer) SimpleLexerBuilder.initLexer(
				new SimpleLexer(), 
				new FileReader(configMap.get(CONFIG_FILE_LEXER)));
		
		lexer.setSourceCode(new FileReader(program));
		tokenList = lexer.getAllTokens();
	}

	public Lexer getLexer() {
		return lexer;
	}

	public List<Token> getTokenList() {
		return tokenList;
	}

	public List<ProductionGroup> getProductionGroups() {
		return productionGroups;
	}

	public DynamicParser getParser() {
		return parser;
	}

	public SimpleSemantic getSemanticParser() {
		return semanticParser;
	}

	public Map<String, Object> getSemanticMap() {
		return semanticMap;
	}

	public Map<String, String> getSymbolMap() {
		return symbolMap;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public SimpleGenerator getGenerator() {
		return generator;
	}

	public Map<String, ICoder> getCoderMap() {
		return coderMap;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}
	
	public boolean isParserOK() {
		return parserOK;
	}

}
