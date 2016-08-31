package br.gui.cc.gen.reader.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandSupportMap extends AbstractCommand {
	private List<String> path;

	@Override
	public boolean isSuitable(String text) {
		return text.startsWith("support_map");
	}
	
	@Override
	public void processParameter(String text) {
		path = new ArrayList<String>(Arrays.asList(text.split(" +")));
		if (path.size() < 2) {
			throw new IllegalArgumentException("Illegal parameter: " + text);
		}
		path.remove(0);
	}

	@Override
	public boolean hasNewLine() {
		return false;
	}

	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		Object obj = generator.getSupportMap().get(path.get(0));
		for (int index = 1; index < path.size(); index++) {
			try {
				obj = invokeGetter(obj, convertAttributeToGetter(path.get(index)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return obj.toString();
	}
	
	private Object invokeGetter(Object obj, String methodName) throws Exception {
		return obj.getClass().getMethod(methodName, null).invoke(obj, null);
	}
	
	private String convertAttributeToGetter(String text) {
		return "get"+ Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandSupportMap();
	}
	
}
