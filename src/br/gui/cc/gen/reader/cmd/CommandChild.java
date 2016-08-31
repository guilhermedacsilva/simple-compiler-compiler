package br.gui.cc.gen.reader.cmd;

import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.parser.ASTNode;

public class CommandChild extends AbstractCommand {

	@Override
	public boolean isSuitable(String text) {
		return text.startsWith("child");
	}

	@Override
	public void processParameter(String text) {
		parameter = text.substring(5).trim();
	}

	/**
	 * @param parameter production number or "self"
	 */
	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		if ("self".equals(parameter)) {
			return generator.generateNode(node);
		}
		return generator.generateNode(node.getChildren().get(Integer.parseInt(parameter)));
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandChild();
	}
	

}
