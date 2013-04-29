package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/*
 * Parameterized test cases - the set of tests run over all input parameters
 */
public abstract class TestSuite {

	// Localized file paths
	protected String pathGrammar = "", pathInput = "", pathSpec = "";

	private String pathOutput = "", pathScript = "";

	// The actual files
	private File fileGrammar, fileInput, fileOutput, fileScript, fileSpec;

	// The texts contained within the files
	protected String textOutput = "";
	
	/*
	 * Constructor for the run of tests
	 */
	public TestSuite(String pathGrammar, String pathInput, String pathOutput, String pathScript, String pathSpec) {
		this.pathGrammar = pathGrammar;
		this.pathInput = pathInput;
		this.pathOutput = pathOutput;
		this.pathScript = pathScript;
		this.pathSpec = pathSpec;
	 }

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
		fileScript = new File(pathScript);
		fileSpec= new File(pathSpec);
	}

	/*
	 * Display all the file names and checks contents
	 */
	private void display() {
		System.out.println(fileGrammar.getAbsolutePath());
		System.out.println(fileInput.getAbsolutePath());
		System.out.println(fileOutput.getAbsolutePath());
		System.out.println(fileScript.getAbsolutePath());
		System.out.println(fileSpec.getAbsolutePath() + "\n");
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
	public abstract void test_all() throws Exception;
}
