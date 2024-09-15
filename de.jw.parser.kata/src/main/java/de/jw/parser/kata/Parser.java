package de.jw.parser.kata;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A parser that converts a string or string array
 * into a HashMap of key-value pairs
 */
public class Parser {
	
	HashMap<String, Object> map;
	
	public Parser() {
		map = new HashMap<>();
	}
	
	/**
	 * Converts a string array of the structure: ["--key1","--key2","value2"]
	 *  to a String-Object HashMap of key-value pairs.
	 *  Keys should be preceded by "--"; if a key is not followed by a value, 
	 *  the value will automatically be true
	 */
	public HashMap<String, Object> parse(String[] args) {
		int i = 0;
		while(i < args.length) {
			String[] nextCommand = findNextCommand(args, i);
			parsePair(nextCommand);
			i+= (nextCommand.length);
		}
		return map;
	}

	/**
	 * Converts a string of the structure: "--key1 --key2 value2"
	 *  to a String-Object HashMap of key-value pairs.
	 *  Keys should be preceded by "--"; if a key is not followed by a value, 
	 *  the value will automatically be true
	 */
	public HashMap<String, Object> parse(String args) {
		String[] commands = args.split("--",0);
		for(String command: commands) {
			if(command.isBlank()) {
				continue;
			}
			parsePair(command.split(" "));
		}
		return map;
	}
	
	private String[] findNextCommand(String[] args, int startIdx) {
		if(startIdx < (args.length - 1) && args[startIdx + 1].contains("--") == false) {
			return new String[] {args[startIdx], args[startIdx + 1]};
		}
		return new String[] {args[startIdx]};
	}
	
	private void parsePair(String[] pair) {
		String key = pair[0].replace("--", "");
		if(key.isEmpty() == false) {
			insertValue(key, pair);
		}
	}
	
	private void insertValue(String key, String[] pair) {
		Object value = parseNewValue(pair);
		if(map.containsKey(key)) {
			value = getArrayValue(key, value);
			
		}
		map.put(key, value);
	}
	
	private Object parseNewValue(String[] commands) {
		Object value = true;
		if(commands.length > 1) {
			try {
				value = Integer.valueOf(commands[1]);
			}
			catch(NumberFormatException ex) {
				value = commands[1];
			}
		}
		return value;
	}

	private Object getArrayValue(String key, Object value) {
		if(map.get(key) instanceof Object[]) {
			Object[] originalValue = (Object[])map.get(key);
			int originalLength = originalValue.length;
			originalValue = Arrays.copyOf(originalValue, originalLength + 1);
			originalValue[originalLength] = value;
			value = originalValue;
		} else {
			value = new Object[] {map.get(key), value};
		}
		return value;
	}
}
