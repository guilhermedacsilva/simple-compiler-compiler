package br.gui.cc.gen.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gui.cc.gen.reader.cmd.AbstractCommand;
import br.gui.cc.gen.reader.cmd.CommandChild;
import br.gui.cc.gen.reader.cmd.CommandIterateBegin;
import br.gui.cc.gen.reader.cmd.CommandIterateEnd;
import br.gui.cc.gen.reader.cmd.CommandLexeme;
import br.gui.cc.gen.reader.cmd.CommandProduction;
import br.gui.cc.gen.reader.cmd.CommandSupportMap;

public class CommandFactory {
	private static final Pattern PATTERN = Pattern.compile("\\#\\{.+\\}");
	private static final List<AbstractCommand> COMMAND_LIST = new ArrayList<AbstractCommand>(4);
	static {
		COMMAND_LIST.add(new CommandChild());
		COMMAND_LIST.add(new CommandProduction());
		COMMAND_LIST.add(new CommandLexeme());
		COMMAND_LIST.add(new CommandIterateBegin());
		COMMAND_LIST.add(new CommandIterateEnd());
		COMMAND_LIST.add(new CommandSupportMap());
	}

	private CommandFactory() {}
	
	public static AbstractCommand createCommand(String text, int lineIndex) {
		text = extractCommandFromText(text);
		for (AbstractCommand cmd : COMMAND_LIST) {
			if (cmd.isSuitable(text)) {
				return cmd.createNew(text, lineIndex);
			}
		}
		throw new IllegalArgumentException("Command unknown: " + text);
	}
	
	
	public static Matcher createMatcher(String text) {
		return PATTERN.matcher(text);
	}
	
	private static String extractCommandFromText(String text) {
		return text.substring(2, text.length()-1).trim();
	}
	
}
