import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	// Why you have problems? I no have problems.
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

        Map<String, String> tokens = new HashMap<String, String>();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contentEquals("\n")) {
                break;
            }
            System.out.println(line);
            Pattern pattern = Pattern.compile("\\$[A-Z]+");
            Matcher matcher = pattern.matcher(line);
            String token = matcher.group();
            String rest = line.substring(matcher.end());
            tokens.put(token, rest);
        }

        for(String key : tokens.keySet()) {
            System.out.println("{" + key + ", " + tokens.get(key) +"}");
        }
    }
}
