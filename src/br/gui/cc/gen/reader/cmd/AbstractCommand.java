package br.gui.cc.gen.reader.cmd;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public abstract class AbstractCommand {
	protected String parameter;
	protected int lineIndex;

	protected abstract AbstractCommand createInstance();

	public String doCode(SimpleGenerator generator, ASTNode node) {
		return "";
	}
	
	public String getUniqueName() {
		return "";
	}

	public boolean isSuitable(String text) {
		return false;
	}

	public void processParameter(String text) {}

	public boolean hasNewLine() {
		return true; 
	}
	
	public Integer goToLine() {
		return null;
	}
	
	public int getLineIndex() {
		return lineIndex;
	}

	public String jumpToCommand() {
		return null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+":"+(parameter == null? "" : parameter);
	}
	
	public AbstractCommand createNew(String text, int lineIndex) {
		AbstractCommand newCommand = createInstance();
		newCommand.lineIndex = lineIndex;
		newCommand.processParameter(text);
		return newCommand;
	}
	
}
