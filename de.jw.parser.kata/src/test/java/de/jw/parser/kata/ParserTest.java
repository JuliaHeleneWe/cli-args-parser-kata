package de.jw.parser.kata;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ParserTest {
	Parser parser;
	
	@BeforeEach
	void createParser() {
		parser = new Parser();
	}
	
	@ParameterizedTest
	@MethodSource("cliTestData")
	void testParser(Object args, HashMap<String, Object> expectedMap) {
		HashMap<String, Object> parsedMap = getParsedMap(args);
		if(parsedMap != null) {
			compareMaps(expectedMap, parsedMap);
		}
	}
	
	private HashMap<String, Object> getParsedMap(Object args) {
		if(args instanceof String) {
			return parser.parse((String)args);
		}
		if(args instanceof String[]) {
			return parser.parse((String[])args);
		}	
		return null;
	}
	
	private void compareMaps(HashMap<String, Object> expected, HashMap<String, Object> actual) {
		assertEquals(expected.size(), actual.size());
		for (Map.Entry<String, Object> entry : expected.entrySet()) {
            String expectedKey = entry.getKey();
            Object value = entry.getValue();
            assertTrue(actual.containsKey(expectedKey));
            Object actualValue = actual.get(expectedKey);
            if(value instanceof Object[] && actualValue instanceof Object[]) {
            	assertArrayEquals((Object[])value, (Object[])actualValue);
            }
            else {
            	assertEquals(value, actualValue);
            }
        }
	}
	
	private static Stream<Arguments> cliTestData() {
	    return Stream.of(
		  Arguments.of("--foo", new HashMap<String, Object>() {{
    	     put("foo", true);
    	  }}),
	      Arguments.of("--foo bar", new HashMap<String, Object>() {{
	    	 put("foo", "bar");
	      }}),
	      Arguments.of("--number 1", new HashMap<String, Object>() {{
	    	    put("number", 1);
	      }}),
	      Arguments.of("--foo --bar baz --number 1", new HashMap<String, Object>() {{
	    	    put("foo", true);
	    	    put("bar", "baz");
	    	    put("number", 1);
	      }}),
	      Arguments.of(new String[]{"--foo","--bar","baz", "--number","1"}, new HashMap<String, Object>() {{
	    	    put("foo", true);
	    	    put("bar", "baz");
	    	    put("number", 1);
	      }}),
	      Arguments.of(new String[]{"--foo","--bar","baz","--bar","zab", "--number","1"}, new HashMap<String, Object>() {{
	    	    put("foo", true);
	    	    put("bar", new Object[]{"baz", "zab"});
	    	    put("number", 1);
	      }}),
	      Arguments.of("--foo --bar baz --bar zab --number 1", new HashMap<String, Object>() {{
	    	    put("foo", true);
	    	    put("bar", new Object[]{"baz", "zab"});
	    	    put("number", 1);
	      }})
	    );
	}

}
