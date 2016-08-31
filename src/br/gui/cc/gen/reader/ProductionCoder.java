package br.gui.cc.gen.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.SimpleGenerator;
import br.gui.cc.gen.reader.cmd.AbstractCommand;
import br.gui.cc.parser.ASTNode;

public class ProductionCoder implements ICoder {
	private static final String REGEX_PRODUCTION = "\\#\\{def +production +(.*)\\} *";
	private static final Pattern PATTERN_NAME = Pattern.compile(REGEX_PRODUCTION);
	private List<AbstractCommand> commandList = new ArrayList<AbstractCommand>(3);
	private String name;
	
	public ProductionCoder(String line) {
		name = extractName(line);
	}
	
	public String getName() {
		return name;
	}
	
	public void addCommand(AbstractCommand command) {
		commandList.add(command);
	}
	
	@Override
	public String doCode(SimpleGenerator generator, ASTNode node) {
		String result = "";
		AbstractCommand command;
		int index = 0;
		String stopUntilCommand = null;
		while (index < commandList.size()) {
			command = commandList.get(index);
			
			if (stopUntilCommand != null) {
				if (!stopUntilCommand.equals(command.getUniqueName())) {
					index++;
					continue;
				}
				stopUntilCommand = null;
			}
			
			result += command.doCode(generator, node);
			
			stopUntilCommand = command.jumpToCommand();
			
			if (command.goToLine() == null) {
				index++;
			} else {
				index = goToLine(command.goToLine());
			}
		}
		return result;
	}
	
	private int goToLine(Integer lineIndex) {
		for (int partIndex = 0; partIndex < commandList.size(); partIndex++) {
			if (commandList.get(partIndex).getLineIndex() == lineIndex) {
				return partIndex;
			}
		}
		throw new RuntimeException("Line not found: " + lineIndex);
	}

	public static boolean isProductionDefinition(String line) {
		return line.matches(REGEX_PRODUCTION);
	}

	private static String extractName(String line) {
		Matcher matcher = PATTERN_NAME.matcher(line);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Unknown production coder: "+line);
		}
		return matcher.group(1);
	}
}
