package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that initializes the file parser
 */
public class TokenParser {

    public static class TokenInfo {
        public Map<String, String> characterClasses;
        public Map<String, String> tokens;
        public List<String> tokenList;
    }

	private String output;

	TokenInfo parse(String pathToGrammar, String pathToInput) {
		// Input and scan the grammar file
		Scanner scannerGrammar = scan(pathToGrammar);

		// Check for comments at the beginning of the file.
		if(scannerGrammar.hasNext()){
			int counter = 0;
			while(scannerGrammar.nextLine().startsWith("%%")){
				counter = counter + 1;
			}
			if(counter == 0){
				scannerGrammar = scan(pathToGrammar);
			}
			else{
				int doubleCount = 0;
				scannerGrammar = scan(pathToGrammar);
				while(doubleCount < counter){
					doubleCount = doubleCount + 1;
					scannerGrammar.nextLine();
				}
			}
		}

        TokenInfo info = new TokenInfo();
        info.tokenList = new ArrayList<String>();

		// Parse the classes and then the tokens
		info.characterClasses = parseClasses(scannerGrammar);
		info.tokens = parseTokens(scannerGrammar, info);

        return info;
	}

	/*
	 * Return the output
	 */
	public String getOutput() {
		return output;
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
	private HashMap<String, String> parseTokens(Scanner scanner, TokenInfo info) {
		// HashMap of tokens and their token type
		HashMap<String, String> tokenMap = new HashMap<String, String>();
        Pattern p = Pattern.compile("\\$([A-Z]+[A-Z-_]+[A-Z]+|[A-Z]+)");
		// Parse for character tokens
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("%%")){
				continue;
			}
			Matcher matcher = p.matcher(line);
			matcher.find();
			String token = matcher.group(0);
            info.tokenList.add(token);
			String rest = removeInitialWhitespace(line.substring(matcher.end()));
			rest = removeUnescapedSpaces(rest);
			tokenMap.put(token, rest);
		}
		return tokenMap;
	}

	/*
	 * Iterates over the lines of the grammar file for classes (before the line
	 * break)
	 */
	private HashMap<String, String> parseClasses(Scanner scanner) {
		// HashMap of classes and their class type
		HashMap<String, String> classMap = new HashMap<String, String>();
		Pattern p = Pattern.compile("\\$([A-Z]+|[A-Z]+[A-Z-_]+[A-Z]+)");
		// Parse for character classes in grammar file
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().equals("")) {
				// A blank line means switch to the token parsing
				break;
			}
			else if(line.startsWith("%%")){
				// A comment means switch to the token parsing
				break;
			}
			Matcher matcher = p.matcher(line);
			matcher.find();
			String charClass = matcher.group(0);
			String rest = removeInitialWhitespace(line.substring(matcher.end()));
			rest = removeUnescapedSpaces(rest);
			classMap.put(charClass, rest);
		}
		return classMap;
	}

	/**
	 * Removes unescaped spaces except in exclude classes
	 * 
	 * @param line
	 * @return
	 */
	private String removeUnescapedSpaces(String line) {
		Pattern p = Pattern.compile("(?<!\\\\|IN|\\[) (?!\\]|IN)");
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
