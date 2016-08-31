package br.gui.cc.all;

import java.util.HashMap;
import java.util.Map;

public class DynamicCompilerBuilder {
	private Map<String, String> configMap;
	
	public DynamicCompilerBuilder() {
		configMap = new HashMap<String, String>();
	}
	
	public DynamicCompilerBuilder setGenFile(String fileName) {
		configMap.put(DynamicCompiler.CONFIG_FILE_GEN, fileName);
		return this;
	}
	
	public DynamicCompilerBuilder setSemanticFile(String fileName) {
		configMap.put(DynamicCompiler.CONFIG_FILE_SEMANTIC, fileName);
		return this;
	}
	
	public DynamicCompilerBuilder setSymbolTableFile(String fileName) {
		configMap.put(DynamicCompiler.CONFIG_FILE_SYMBOL_TABLE, fileName);
		return this;
	}
	
	public DynamicCompilerBuilder setParserFile(String fileName) {
		configMap.put(DynamicCompiler.CONFIG_FILE_PARSER, fileName);
		return this;
	}
	
	public DynamicCompilerBuilder setLexerFile(String fileName) {
		configMap.put(DynamicCompiler.CONFIG_FILE_LEXER, fileName);
		return this;
	}
	
	public DynamicCompiler build() {
		DynamicCompiler compiler = new DynamicCompiler();
		compiler.setConfigMap(configMap);
		return compiler;
	}

}
