package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that initializes the file parser
 */
public class TokenParser {

	Map<String, String> parse(String pathToGrammar) {
		// Input and scan the grammar file
		Scanner scannerGrammar = scan(pathToGrammar);

		// Parse the classes and then the tokens
		return parseTokens(scannerGrammar);
	}


	/*
	 * Take file path and return the scanner
	 */
	private Scanner scan(String path) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.exit(-1);
		}
		return scanner;
	}

	/*
	 * Iterates over the grammar file after the line break (so for tokens)
	 */
	private HashMap<String, String> parseTokens(Scanner scanner) {
		// HashMap of tokens and their token type
		HashMap<String, String> tokenMap = new HashMap<String, String>();
		Pattern p = Pattern.compile("\\$([A-Z]+(-|_)?[A-Z]+)|[A-Z]+");
		// Parse for character tokens
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("%%") || line.trim().isEmpty()){
				continue;
			}
			Matcher matcher = p.matcher(line);
			matcher.find();
			String token = matcher.group(0);
			String rest = removeInitialWhitespace(line.substring(matcher.end()));
			rest = removeUnescapedSpaces(rest);
			tokenMap.put(token, rest);
		}
		return tokenMap;
	}

	/**
	 * Removes unescaped spaces except in exclude classes
	 * 
	 * @param line
	 * @return
	 */
	private String removeUnescapedSpaces(String line) {
		Pattern p = Pattern.compile("(?<!\\\\|IN|\\[) (?!IN)");
		Matcher matcher = p.matcher(line);
		line = matcher.replaceAll("");
		return line;
	}

	/*
	 * Removes all whitespace from a string
	 */
	private String removeInitialWhitespace(String substring) {
		Pattern initialWhitespace = Pattern.compile("^\\s+");
		Matcher whitespaceMatcher = initialWhitespace.matcher(substring);
		return whitespaceMatcher.replaceAll("");
	}

}
