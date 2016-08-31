package br.gui.cc.semantic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

/**
 * Context-Free Grammar Reader
 */
public class SymbolTableReader {
	
	private SymbolTableReader() {}
	
	public static HashMap<String, String> createSymbolMap(Reader reader)
			throws IOException {
		
		HashMap<String, String> symbolMap = new HashMap<String, String>();
		BufferedReader bReader = new BufferedReader(reader);
		String[] parts;
		String line = bReader.readLine();
		
		while (line != null) {
			line = line.trim();
			if (line.isEmpty()) {
				line = bReader.readLine();
				continue;
			}
			parts = line.split(" +");
			if (parts.length != 2) {
				throw new RuntimeException("More than 2 parts: " + parts.length);
			}
			symbolMap.put(parts[0], parts[1]);
			line = bReader.readLine();
		}
		return symbolMap;
	}

}
