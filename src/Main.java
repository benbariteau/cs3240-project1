import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	/*
	 * Main method and driver of the program
	 * @arg[0]	Grammar
	 * @arg[1]	Input
	 */
    public static void main(String[] args) {
    	// Must have two argument inputs - grammar and sample input
        if (args.length != 2) {
            System.out.println("Invalid parameters. Try:\njava Main <path/to/grammar> <path/to/input>");
            System.exit(-1);
        }

        String grammar = args[0];
        String input = args[1];

        // Input the grammar file
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(grammar));
        } catch (FileNotFoundException e) {
            System.err.println("Grammar file not found");
            System.exit(-1);
        }

        // HashMap of classes and their class type
        Map<String, String> characterClasses = new HashMap<String, String>();

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
            characterClasses.put(charClass, rest);
        }

        // Output all the character classes that we just scanned
        System.out.println("Character Classes");
        for(String key : characterClasses.keySet()) {
            System.out.println("{" + key + ", " + characterClasses.get(key) +"}");
        }
        System.out.println();

        // HashMap of tokens and their token type
        Map<String, String> tokens = new HashMap<String, String>();

        // Parse for character tokens
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String token = matcher.group(0);
            String rest = removeInitialWhitespace(line.substring(matcher.end()));
            tokens.put(token, rest);
        }

        // Output all the character tokens that we just scanned
        System.out.println("Tokens");
        for(String key : tokens.keySet()) {
            System.out.println("{" + key + ", " + tokens.get(key) +"}");
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
