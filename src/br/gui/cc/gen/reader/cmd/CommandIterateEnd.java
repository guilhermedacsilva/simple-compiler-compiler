package br.gui.cc.gen.reader.cmd;

public class CommandIterateEnd extends AbstractCommand {
	public static final String UNIQUE_NAME_PREFIX = "IT_END_";
	private CommandIterateBegin begin;
	private String uniqueName;
	
	@Override
	public boolean isSuitable(String text) {
		final String[] wordArray = text.split(" +");
		return wordArray.length == 3
				&& wordArray[0].trim().equals("end")
				&& wordArray[1].trim().equals("iterate");
	}
	
	@Override
	public String getUniqueName() {
		return uniqueName;
	}

	@Override
	public void processParameter(String text) {
		final String[] wordArray = text.split(" +");
		String varName = wordArray[2];
		uniqueName = UNIQUE_NAME_PREFIX+varName;
		begin = CommandIterateBegin.findByName(CommandIterateBegin.UNIQUE_NAME_PREFIX+varName);
	}
	
	@Override
	public Integer goToLine() {
		if (begin.isActive()) {
			return begin.getLineIndex();
		}
		return null;
	}

	@Override
	protected AbstractCommand createInstance() {
		return new CommandIterateEnd();
	}
	
}
