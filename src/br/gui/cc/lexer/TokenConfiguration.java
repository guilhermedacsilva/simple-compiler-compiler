package br.gui.cc.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TokenConfiguration {
	private String className;
	private List<Pattern> patternList;
	
	public TokenConfiguration(String className) {
		this.className = className;
		patternList = new ArrayList<Pattern>(1);
	}
	
	public String getClassName() {
		return className;
	}

	public List<Pattern> getPatternList() {
		return patternList;
	}
	
}
