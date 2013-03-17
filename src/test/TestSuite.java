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
 * These tests run with the examples given on T-Square
 */
@RunWith(value = Parameterized.class)
public class TestSuite {

	/*
	 * The list of parameter groups to use for the tests
	 * 
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "resources/SampleGrammar", "resources/SampleInput", "resources/SampleOutput" },
				{ "resources/SampleGrammar", "resources/SampleInput", "resources/SampleOutput" },
				{ "resources/SampleGrammar", "resources/SampleInput", "resources/SampleOutput" }
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
