package br.gui.cc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ReaderUtil {
	private String content;
	private int position;
	
	public ReaderUtil(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		StringBuilder builder = new StringBuilder();
		String line;
		while ( (line = bufferedReader.readLine() ) != null) {
			builder.append(line).append("\n");
		}
		bufferedReader.close();
		content = builder.toString();
	}
	
	public static ReaderUtil createFromFile(String filename) throws IOException {
		return new ReaderUtil(new FileReader(filename));
	}
	
	public static ReaderUtil createFromString(String content) throws IOException {
		return new ReaderUtil(new StringReader(content));
	}
	
	public void rewind() {
		position = 0;
	}

	public boolean isDone() {
		return isDoneOffset(0);
	}

	public boolean isDoneOffset(int offset) {
		return position + offset >= content.length();
	}
	
	public boolean isWhitespace() {
		return Character.isWhitespace(getCurrentChar());
	}
	
	public char getCurrentChar() {
		return content.charAt(position);
	}
	
	public char getNextChar() {
		return content.charAt(position+1);
	}

	public void walkPosition() {
		walkPosition(1);
	}
	
	public void walkPosition(int step) {
		position += step;
	}
	
}
