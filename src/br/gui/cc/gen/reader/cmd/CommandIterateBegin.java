package br.gui.cc.gen.reader.cmd;

import java.util.LinkedList;
import java.util.List;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandIterateBegin extends AbstractCommand {
	public static final String UNIQUE_NAME_PREFIX = "IT_BEG_";
	private static final List<CommandIterateBegin> REGISTER_ITERATE = new LinkedList<CommandIterateBegin>();
	private String varName;
	private String listName;
	private List listObject;
	private int iterationNumber = 0;
	private String uniqueName;
	private String uniqueNameEnd;
	private boolean active;
	
	@Override
	public boolean isSuitable(String text) {
		final String[] wordArray = text.split(" +");
		return wordArray.length == 5
				&& wordArray[0].trim().equals("begin")
				&& wordArray[1].trim().equals("iterate")
				&& wordArray[3].trim().equals("=");
	}
	
	@Override
	public String getUniqueName() {
		return uniqueName;
	}

	@Override
	public String jumpToCommand() {
		return active ? null : uniqueNameEnd;
	}

	@Override
	public void processParameter(String text) {
		/**
		 *   0     1       2    3      4
		 * begin iterate symbol = symbol_table
		 */
		final String[] wordArray = text.split(" +");
		varName = wordArray[2];
		listName = wordArray[4];
		uniqueName = UNIQUE_NAME_PREFIX+varName;
		uniqueNameEnd = CommandIterateEnd.UNIQUE_NAME_PREFIX+varName;
		REGISTER_ITERATE.add(this);
	}

	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		active = true;
		createListForIteration(generator);
		if (listObject.isEmpty() || iterationNumber == listObject.size()) {
			jumpToEnd();
		} else {
			updateSupportMap(generator);
		}
		return "";
	}

	private void createListForIteration(SimpleGenerator generator) {
		if (listObject != null) {
			return;
		}
		if (listName.equals("symbol_table")) {
			listObject = generator.getSymbolTable().getVarList();
		
		} else {
			throw new IllegalArgumentException("Unknown list for iteration: " + listName);
		}
	}
	
	private void jumpToEnd() {
		active = false;
		iterationNumber = 0;
	}
	
	private void updateSupportMap(SimpleGenerator generator) {
		generator.getSupportMap().put(varName, listObject.get(iterationNumber));
		iterationNumber++;
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandIterateBegin();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public static CommandIterateBegin findByName(String name) {
		for (int i = REGISTER_ITERATE.size()-1; i >= 0; i--) {
			if (REGISTER_ITERATE.get(i).getUniqueName().equals(name)) {
				return REGISTER_ITERATE.get(i);
			}
		}
		throw new RuntimeException("Iterate Begin not found: " + name);
	}

}
