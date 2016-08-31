package br.gui.cc.parser;

import java.util.ArrayList;

/**
 * The production cannot be empty
 */
public class Production extends ArrayList<Symbol> {
	private static final long serialVersionUID = 1L;
	private ProductionGroup group;
	private String name;
	
	public Production() {
		super(10);
	}
	
	public void setGroup(ProductionGroup group) {
		this.group = group;
	}
	
	public ProductionGroup getGroup() {
		return group;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	@Override
	public String toString() {
		String msg = "";
		for (Symbol s : this) {
			msg += s + " ";
		}
		return msg;
	}
	
}
