package br.gui.cc.gen.reader.cmd;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandString extends AbstractCommand {
	
	public CommandString(String text, int lineIndex) {
		parameter = text;
		this.lineIndex = lineIndex;
	}
	
	public CommandString(String text, int start, int end, int lineIndex) {
		parameter = text.substring(start, end);
		this.lineIndex = lineIndex;
	}

	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		return parameter;
	}

	@Override
	protected AbstractCommand createInstance() {
		return null;
	}

}
