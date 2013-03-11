import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	private static Map<String, String> mapClasses, mapTokens;
	private static String pathToGrammar, pathToInput;
	
	/*
	 * Main method and driver of the program
	 * @arg[0]	Grammar
	 * @arg[1]	Input
	 */
    public static void main(String[] args) {
    	
    	// Ensure proper input parameters
    	inputValidation(args);

        // The two file paths (grammar and input file respectively)
        pathToGrammar = args[0];
        pathToInput = args[1];
        
        // Input and scan the grammar file
        Scanner scannerGrammar = scan(pathToGrammar);

        // Parse the classes and then the tokens
        Pattern pattern = parseClasses(scannerGrammar);
        parseTokens(scannerGrammar, pattern);
        
        // TODO - input the input file
    }

    /*
     * Take file path and return the scanner
     */
	private static Scanner scan(String path) {
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
     * Test inputs to ensure validity
     */
	private static void inputValidation(String[] args) {
		// Must have two argument inputs - grammar and sample input
        if (args.length != 2) {
            System.out.println("Invalid parameters. Try:\njava Main <path/to/grammar> <path/to/input>");
            System.exit(-1);
        }
	}

    /*
     * Iterates over the lines of the grammar file for classes (before the line break)
     */
	private static Pattern parseClasses(Scanner scanner) {
		// HashMap of classes and their class type
        mapClasses = new HashMap<String, String>();

        // Parse for character classes in grammar file
        Pattern pattern = Pattern.compile("\\$[A-Z-]+");
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals("")) {
            	// A blank line means switch to the token parsing
                break;
            }
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String charClass = matcher.group(0);
            String rest = removeInitialWhitespace(line.substring(matcher.end()));
            mapClasses.put(charClass, rest);
        }

        // Output all the character classes that we just scanned
        System.out.println("Character Classes:");
        for(String key : mapClasses.keySet()) {
            System.out.println("{" + key + ", " + mapClasses.get(key) +"}");
        }
        System.out.println();
		return pattern;
	}

	/*
	 * Iterates over the grammar file after the line break (so for tokens)
	 */
	private static void parseTokens(Scanner scanner, Pattern pattern) {
		// HashMap of tokens and their token type
        mapTokens = new HashMap<String, String>();

        // Parse for character tokens
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String token = matcher.group(0);
            String rest = removeInitialWhitespace(line.substring(matcher.end()));
            mapTokens.put(token, rest);
        }

        // Output all the character tokens that we just scanned
        System.out.println("Tokens:");
        for(String key : mapTokens.keySet()) {
            System.out.println("{" + key + ", " + mapTokens.get(key) +"}");
        }
	}

    /*
     * Removes all whitespace from a string
     */
    private static String removeInitialWhitespace(String substring) {
        Pattern initialWhitespace = Pattern.compile("^\\s+");
        Matcher whitespaceMatcher = initialWhitespace.matcher(substring);
        return whitespaceMatcher.replaceAll("");
    }
    
}
