package br.gui.cc.gen.reader.cmd;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandLexeme extends AbstractCommand {

	@Override
	public boolean isSuitable(String text) {
		return text.equals("lexeme");
	}

	@Override
	public boolean hasNewLine() {
		return false;
	}

	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		return node.getToken().getLexeme();
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandLexeme();
	}
	
}
