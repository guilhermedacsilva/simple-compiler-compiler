package br.gui.cc.gen.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.regex.Matcher;

import br.gui.cc.gen.ICoder;
import br.gui.cc.gen.reader.cmd.AbstractCommand;
import br.gui.cc.gen.reader.cmd.CommandString;

public class GeneratorReader {
	private HashMap<String, ICoder> coderMap = new HashMap<String, ICoder>();
	private ProductionCoder currentCoder;
	
	public HashMap<String, ICoder> createCoderMap(Reader reader) throws IOException {
		coderMap.clear();
		BufferedReader breader = new BufferedReader(reader);
		String line = breader.readLine();
		currentCoder = null;
		int lineIndex = 0;
		while (line != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				if (ProductionCoder.isProductionDefinition(line)) {
					currentCoder = new ProductionCoder(line);
					coderMap.put(currentCoder.getName(), currentCoder);
					lineIndex = 0;
				} else {
					if (currentCoder == null) {
						throw new RuntimeException("Unknown production coder syntax: "+line);
					}
					createCommand(line, lineIndex);
					lineIndex++;
				}
			}
			line = breader.readLine();
		}
		return coderMap;
	}
	
	private void createCommand(String line, int lineIndex) {
		final Matcher matcher = CommandFactory.createMatcher(line);
		int matcherLastEnd = 0;
		AbstractCommand command = null;
		while (matcher.find()) {
			if (matcher.start() > 0) {
				createCommandString(line, matcherLastEnd, matcher.start(), lineIndex);
			}
			command = CommandFactory.createCommand(matcher.group(), lineIndex);
			currentCoder.addCommand(command);
			matcherLastEnd = matcher.end();
		}
		if (matcherLastEnd < line.length()) {
			createCommandString(line+"\n", matcherLastEnd, lineIndex);
		} else if (command == null || !command.hasNewLine()) {
			createCommandNewLine(lineIndex);
		}
	}
	
	private void createCommandString(String line, int start, int lineIndex) {
		currentCoder.addCommand(new CommandString(line, start, line.length(),lineIndex));
	}
	
	private void createCommandString(String line, int start, int end, int lineIndex) {
		currentCoder.addCommand(new CommandString(line, start, end,lineIndex));
	}
	
	private void createCommandNewLine(int lineIndex) {
		currentCoder.addCommand(new CommandString("\n", lineIndex));
	}
	
}
