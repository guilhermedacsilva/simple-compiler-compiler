package br.gui.cc.gen.reader.cmd;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandProduction extends AbstractCommand {

	@Override
	public boolean isSuitable(String text) {
		return text.startsWith("production");
	}

	@Override
	public void processParameter(String text) {
		parameter = text.substring(10).trim();
	}

	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		return generator.generateProduction(node, parameter);
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandProduction();
	}
	

}
