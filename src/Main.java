import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main path/to/grammar path/to/input");
            System.exit(-1);
        }

        String grammar = args[0];
        String input = args[1];

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(grammar));
        } catch (FileNotFoundException e) {
            System.err.println("Grammar file not found");
            System.exit(-1);
        }

        Map<String, String> characterClasses = new HashMap<String, String>();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Pattern pattern = Pattern.compile("\\$\\S+");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String charClass = matcher.group(0);
            String rest = line.substring(matcher.end());
            characterClasses.put(charClass, rest);
        }

        System.out.println("Character Classes");
        for(String key : characterClasses.keySet()) {
            System.out.println("{" + key + ", " + characterClasses.get(key) +"}");
        }
        System.out.println();

        Map<String, String> tokens = new HashMap<String, String>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Pattern pattern = Pattern.compile("\\$\\S+");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String token = matcher.group(0);
            String rest = line.substring(matcher.end());
            tokens.put(token, rest);
        }

        System.out.println("Tokens");
        for(String key : tokens.keySet()) {
            System.out.println("{" + key + ", " + tokens.get(key) +"}");
        }
    }
}
