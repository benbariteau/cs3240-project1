import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	// BALRHGINGKHGLSJLDS Behl.
    public static void main(String[] args) {
        String grammar = args[0];
        String input = args[1];
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(grammar));
        } catch (FileNotFoundException e) {
            System.err.println("Grammar file not found");
            System.exit(-1);
        }

        if(scanner.hasNext()) {
            System.out.println(scanner.next());
        }
    }
}
