package test;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import main.Main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * Parameterized test cases - the set of tests run over all input parameters
 */
@RunWith(value = Parameterized.class)
public class InvalidTestSuite {

	/*
	 * The following test cases are invalid and should fail:
	 * set0
	 * set1
	 * set2
	 * set3
	 * set4
	 * set5
	 * set6
	 * set7
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "resources/invalid/set0/SampleGrammar", "resources/invalid/set0/SampleInput", "resources/invalid/set0/SampleOutput" },
				{ "resources/invalid/set1/SampleGrammar", "resources/invalid/set1/SampleInput", "resources/invalid/set1/SampleOutput" },
				{ "resources/invalid/set2/SampleGrammar", "resources/invalid/set2/SampleInput", "resources/invalid/set2/SampleOutput" },
				{ "resources/invalid/set3/SampleGrammar", "resources/invalid/set3/SampleInput", "resources/invalid/set3/SampleOutput" },
				{ "resources/invalid/set4/SampleGrammar", "resources/invalid/set4/SampleInput", "resources/invalid/set4/SampleOutput" },
				{ "resources/invalid/set5/SampleGrammar", "resources/invalid/set5/SampleInput", "resources/invalid/set5/SampleOutput" },
				{ "resources/invalid/set6/SampleGrammar", "resources/invalid/set6/SampleInput", "resources/invalid/set6/SampleOutput" },
				{ "resources/invalid/set7/SampleGrammar", "resources/invalid/set7/SampleInput", "resources/invalid/set7/SampleOutput" }
		};
		return Arrays.asList(data);
	}

	/*
	 * Constructor for the run of tests
	 */
	public InvalidTestSuite(String pathGrammar, String pathInput, String pathOutput) {
		this.pathGrammar = pathGrammar;
		this.pathInput = pathInput;
		this.pathOutput = pathOutput;
	 }
	
	// Localized file paths
	private String pathGrammar = "", pathInput = "", pathOutput = "";

	// The actual files
	private File fileGrammar, fileInput, fileOutput;

	// The texts contained within the files
	private String textOutput = "";

	/*
	 * Setup the test environment
	 */
	@Before
	public void setUp() {
		System.out.println("Files:");
		// Get the files ready
		setUpFiles();
		// Display file information
		display();
	}

	/*
	 * Creates all of the file objects from the given paths
	 */
	private void setUpFiles() {
		fileGrammar = new File(pathGrammar);
		fileInput = new File(pathInput);
		fileOutput = new File(pathOutput);
	}

	/*
	 * Display all the file names and checks contents
	 */
	private void display() {
		System.out.println(fileGrammar.getAbsolutePath());
		System.out.println(fileInput.getAbsolutePath());
		System.out.println(fileOutput.getAbsolutePath() + "\n");
		textOutput = displayFile(fileOutput);
	}

	/*
	 * Displays the file name and the contents to output
	 * Also initializes the text contents of each file
	 */
	private String displayFile(File file) {
		// Read in the file contents
        String text = "";
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// If no file found, exit execution
			System.err.println("File not found:");
			System.err.println(file.getAbsolutePath());
			System.exit(-1);
		}
		// Go line-by-line and read the file
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// Adds a newline character to the end of the string
			text += line + System.getProperty("line.separator");
		}
		// Print the final string which represents the sum file contents
		System.out.println(file.getName() + ":\n" + text);
		scanner.close();
        return text;
	}

	/*
	 * 
	 * TEST CASES
	 */

	/*
	 * A single correct run
	 */
	@Test
	public void test_all() throws IOException {
		String s = new Main().run(new String[] { pathGrammar, pathInput });
        System.out.println(s);
        assertFalse(s.equals(textOutput));
	}

}
