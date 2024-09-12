package de.jw.parser.kata;

import java.util.HashMap;

public class Parser {
	
	HashMap<String, Object> map;
	
	public Parser() {
		map = new HashMap<>();
	}
	
	public HashMap<String, Object> parse(String[] args) {
		for(int i = 0; i < args.length; i++) {
			String[] nextCommand = findNextCommand(args, i);
			parsePair(nextCommand);
		}
		return map;
	}

	public HashMap<String, Object> parse(String args) {
		String[] commands = args.split("--");
		for(String command: commands) {
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
		Object value = getValue(pair);
		map.put(key, value);
	}
	
	private Object getValue(String[] commands) {
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
}
