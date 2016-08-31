package br.gui.cc;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import br.gui.cc.lexer.Lexer;
import br.gui.cc.lexer.Token;
import br.gui.cc.lexer.TokenConfiguration;
import br.gui.cc.lexer.simple.SimpleLexer;
import br.gui.cc.lexer.simple.SimpleLexerBuilder;
import br.gui.cc.output.TokenPrinter;
import br.gui.cc.util.ReaderUtil;

/**
 * The main is NOT DONE.
 * The main is NOT WORKING.
 * This main was used to test HTML page.
 */
public class Main {
	/*
	private static Properties props;

	public static void main(String[] args) throws Exception {
		tryLoadProperties(args);
		run();
	}
	
	private static void tryLoadProperties(String[] args) {
		args = new String[] {"html.properties","site.html"};
		if (args.length == 0) {
			System.out.println("Usage: propertiesFile inputFile");
			System.exit(1);
		}
		props = new Properties();
		try {
			props.load(new FileInputStream(args[0]));
		} catch (IOException e) {
			System.out.println("File does not exist.");
			System.exit(1);
		}
		if (args.length > 1) {
			props.setProperty("input", args[1]);
		}
	}
	
	private static void run() throws Exception {
		ReaderUtil lexerConfig = ReaderUtil.createFromFile(props.getProperty("lexer.config"));
		List<TokenConfiguration> tokenConfigList = 
				new DynLexerBuilderOld(lexerConfig).createConfigForLexer();
		Lexer lexer = new DynLexer(tokenConfigList); 
		lexer.loadFromReader(new FileReader(props.getProperty("input")));
		
		List<Token> tokenList = new LinkedList<Token>();
		Token token = lexer.getNextToken();
		
		List<String> lista = new ArrayList<String>(25);
		lista.add("<title");
		lista.add("<h1");
		lista.add("<p>");
		lista.add("<br");
		lista.add("<strong");
		lista.add("<em>");
		lista.add("<hr>");
		lista.add("<blockquote");
		lista.add("<address");
		lista.add("<ol>");
		lista.add("<ul>");
		lista.add("<dl>");
		lista.add("<a ");
		lista.add("<table");
		lista.add("<tr>");
		lista.add("<td>");
		lista.add("<th>");
		lista.add("<form");
		lista.add("<input");
		lista.add("<textarea");
		lista.add("<select");
		
		
		Map<String, Integer> counter = new HashMap<String, Integer>();
		for (String s : lista) {
			counter.put(s, 0);
		}		
		
		while (!token.isEOF()) {
			for (String e : lista) {
				if (token.getLexeme().startsWith(e)) {
					counter.put(e, counter.get(e)+1);
					break;
				}
			}
			tokenList.add(token);
			token = lexer.getNextToken();
		}
		
		for (String s : lista) {
			System.out.println(s + ": " + counter.get(s));
		}
		System.exit(0);
		
		TokenPrinter.print(tokenList);
	}
	
//	private static Object loadClass(String className) {
//		try {
//			return ClassLoader.getSystemClassLoader().loadClass(className).newInstance();
//		} catch (Exception e) {}
//		System.out.println("Class not found: " + className);
//		System.exit(1);
//		return null;
//	}

	*/
}
