package br.gui.cc.semantic;

import java.util.ArrayList;
import java.util.List;

public class TypeConflict {
	private List<TypeObject> conflictList = new ArrayList<TypeObject>();
	private TypeObject lastType;
	
	public void createRule(String type) {
		lastType = new TypeObject(type);
		conflictList.add(lastType);
	}
	
	public void addTypeToRule(String type) {
		lastType.typeList.add(type);
	}
	
	public String resolve(String type1, String type2) {
		for (TypeObject typeObject : conflictList) {
			if (typeObject.typeList.contains(type1)
					&& typeObject.typeList.contains(type2)) {
				return typeObject.result;
			}
		}
		for (TypeObject typeObject : conflictList) {
			if (typeObject.typeList.contains("*")) {
				if (typeObject.result.equals(type1)) {
					return typeObject.result;
				}
				if (typeObject.result.equals(type2)) {
					return typeObject.result;
				}
			}
		}
		throw new IllegalArgumentException("No rule for conflict: " + type1 + " and " + type2);
	}
	
	private class TypeObject {
		private String result;
		private List<String> typeList = new ArrayList<String>(2);
		
		public TypeObject(String result) {
			this.result = result;
		}
	}

}
