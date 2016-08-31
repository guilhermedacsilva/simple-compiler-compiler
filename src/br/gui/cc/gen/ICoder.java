package br.gui.cc.gen;

import br.gui.cc.parser.ASTNode;

public interface ICoder {

	String doCode(SimpleGenerator generator, ASTNode node);
	
}
