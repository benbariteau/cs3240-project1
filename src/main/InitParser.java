package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitParser {

	
	public static void main(String[] args)
	{
		InitParser parser = new InitParser();
		
		parser.parse("resources/set1/SampleGrammar", "resources/set1/SampleInput");

		System.out.println(parser.getOutput());
	}
	
	public InitParser()
	{
	
	}
	private Map<String, String> mapClasses, mapTokens;
	private Map<String, CharacterClass> characterClasses, tokenClasses;
	private String output;
	Map<String, String>[] parse(String pathToGrammar, String pathToInput) {
		// Input and scan the grammar file
		Scanner scannerGrammar = scan(pathToGrammar);

		// Parse the classes and then the tokens
		mapClasses = parseClasses(scannerGrammar);
		mapTokens = parseTokens(scannerGrammar);
		characterClasses = createCharacterClasses();
		
		for(String key:characterClasses.keySet())
		{
			//System.out.println("Key: "+key+" Value: "+characterClasses.get(key).toString());
		}
		tokenClasses = createTokenClasses();
		output = stringOutput(pathToInput);
		
		

		Map<String, String>[] mapList = new Map[2];
		mapList[0] = mapClasses;
		mapList[1] = mapTokens;

		return mapList;
	}
	
	public String getOutput()
	{
		return output;
	}
	
	private String stringOutput(String pathToInput)
	{

		Scanner scannerInput = scan(pathToInput);
		String temp = "";
		while(scannerInput.hasNext())
		{
			String line = scannerInput.nextLine();
			String[] lineSplit = line.split("\\s");
			
			for(String token : lineSplit)
			{
				for(String key:tokenClasses.keySet())
				{
					if(tokenClasses.get(key).matches(token))
					{
						temp += key.replace("$","")+" "+token+"\n";
					}
				}
			}
		}
		return temp;
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
		Pattern p = Pattern.compile("\\$[A-Z-]+");
		// Parse for character tokens
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Matcher matcher = p.matcher(line);
			matcher.find();
			String token = matcher.group(0);
			String rest = removeInitialWhitespace(line.substring(matcher.end()));
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
		Pattern p = Pattern.compile("\\$[A-Z-]+");
		// Parse for character classes in grammar file
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().equals("")) {
				// A blank line means switch to the token parsing
				break;
			}
			Matcher matcher = p.matcher(line);
			matcher.find();
			String charClass = matcher.group(0);
			String rest = removeInitialWhitespace(line.substring(matcher.end()));
			classMap.put(charClass, rest);
		}
		return classMap;
	}
	

	/*
	 * Removes all whitespace from a string
	 */
	private String removeInitialWhitespace(String substring) {
		Pattern initialWhitespace = Pattern.compile("^\\s+");
		Matcher whitespaceMatcher = initialWhitespace.matcher(substring);
		return whitespaceMatcher.replaceAll("");
	}
	
	private HashMap<String, CharacterClass> createCharacterClasses()
	{
		HashMap<String, CharacterClass> tempMap = new HashMap<String, CharacterClass>();
		for(String key:mapClasses.keySet())
		{
			String value = mapClasses.get(key);
			CharacterClass tempClass;
			
			// Find the "superclass" for this character class and pass it in the constructor
			if(value.toUpperCase().contains("IN"))
			{
				String[] valSplit = value.split("\\s");
				tempClass = new CharacterClass(valSplit[0],tempMap.get(valSplit[2]));
			}
			//Otherwise, just give the class the regex
			else
			{
			tempClass = new CharacterClass(mapClasses.get(key));
			}
			
			tempMap.put(key, tempClass);
		}
		return tempMap;
	}
	
	private HashMap<String, CharacterClass> createTokenClasses()
	{
		HashMap<String, CharacterClass> tempMap = new HashMap<String, CharacterClass>();
		for(String key:mapTokens.keySet())
		{
			String value = mapTokens.get(key);
			CharacterClass tempClass;
			
			String[] valSplit = value.split("\\s");
			String temp = "";
			for(String split : valSplit)
			{
				for(String skey:characterClasses.keySet())
				{
					if(split.contains(skey))
					{
						split = split.replace(skey,characterClasses.get(skey).toString());
					}
				}
				temp += split;
			}
			//System.out.println("Key: "+key+" regex: "+temp);
			tempMap.put(key, new CharacterClass(temp));
		}
		return tempMap;
	}

}
