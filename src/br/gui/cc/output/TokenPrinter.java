package br.gui.cc.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.gui.cc.lexer.Token;

public class TokenPrinter {
	private static final String FILENAME_FORMAT = "tokens_%013d.txt";
	private static final String TOKEN_FORMAT = "%03d %s %s\n";
	
	private static File createFile() throws IOException {
		final String name = String.format(FILENAME_FORMAT, System.currentTimeMillis());
		File file = new File(name);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	public static void print(List<Token> tokenList) throws IOException {
		final File file = createFile();
		final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		String msg;
		for (Token token : tokenList) {
			msg = String.format(TOKEN_FORMAT, 
					token.getLineNumber(),
					token.getTokenClass(),
					token.getLexeme());
			System.out.print(msg);
			writer.write(msg);
		}
		writer.close();
	}

}
