package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
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
public class TestSuite {

	/*
	 * The list of parameter groups to use for the tests
	 * set1 is the samples given out on T-Square
	 * set2 is a set with a simple grammar and a simple input
	 * set3 is a set with a simple grammar but a complex input
	 * set4 is a set with a complex grammar but a simple input
	 * set5 is a set with a complex grammar and a complex input
	 * 
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "resources/set1/SampleGrammar", "resources/set1/SampleInput", "resources/set1/SampleOutput" },
				{ "resources/set2/SampleGrammar", "resources/set2/SampleInput", "resources/set2/SampleOutput" },
				{ "resources/set3/SampleGrammar", "resources/set3/SampleInput", "resources/set3/SampleOutput" },
				{ "resources/set4/SampleGrammar", "resources/set4/SampleInput", "resources/set4/SampleOutput" },
				{ "resources/set5/SampleGrammar", "resources/set5/SampleInput", "resources/set5/SampleOutput" }
		};
		return Arrays.asList(data);
	}

	/*
	 * Constructor for the run of tests
	 */
	public TestSuite(String pathGrammar, String pathInput, String pathOutput) {
		this.pathGrammar = pathGrammar;
		this.pathInput = pathInput;
		this.pathOutput = pathOutput;
	 }
	
	// Localized file paths
	private String pathGrammar = "", pathInput = "", pathOutput = "";

	// The actual files
	private File fileGrammar, fileInput, fileOutput;

	// The texts contained within the files
	private final String textGrammar = "", textInput = "", textOutput = "";

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
		displayFile(fileGrammar, textGrammar);
		displayFile(fileInput, textInput);
		displayFile(fileOutput, textOutput);
	}

	/*
	 * Displays the file name and the contents to output
	 * Also initializes the text contents of each file
	 */
	private void displayFile(File file, String text) {
		// Read in the file contents
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
	}

	/*
	 * 
	 * TEST CASES
	 */

	/*
	 * A single correct run
	 */
	@Test
	public void test_all() {
		Main.main(new String[] { pathGrammar, pathInput });
		assertTrue(true);
	}

}
