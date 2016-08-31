package br.gui.cc.parser;

import java.util.Collection;
import java.util.LinkedList;

public class ProductionGroup extends LinkedList<Production> {
	private static final long serialVersionUID = 1L;

	private String name;
	
	public ProductionGroup(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean add(Production e) {
		e.setGroup(this);
		return super.add(e);
	}
	
	@Override
	public void add(int index, Production e) {
		e.setGroup(this);
		super.add(index, e);
	}
	
	public static ProductionGroup findGroup(
			Collection<ProductionGroup> groups,
			String name) {
		
		for (ProductionGroup group : groups) {
			if (group.name.equals(name)) return group;
		}
		throw new RuntimeException("The group " + name + " does not exist.");
	}
}
