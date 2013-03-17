package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * These tests run with the examples given on T-Square
 */
public class SampleTest {

	// Localized file paths
	private final String pathGrammar = "resources/SampleGrammar", pathInput = "resources/SampleInput", pathOutput = "resources/SampleOutput";
	
	// The actual files
	private File fileGrammar, fileInput, fileOutput;
	
	// The texts contained within the files
	private final String textGrammar = "", textInput = "", textOutput = "";
	
	/*
	 * Runs all of the test cases
	 */
	@Test
	public void test_all() {
		Main.main(new String[] { pathGrammar, pathInput }); // TODO - actually compare the output to the sample output
		assertTrue(true);
	}

	/*
	 * Setup the test environment
	 */
	@Before
	public void setUp() {
		System.out.println("Running with samples:");
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
		displayFile(fileGrammar, textGrammar);
		displayFile(fileInput, textInput);
		displayFile(fileOutput, textOutput);
	}
	
	/*
	 * Displays the file name and the contents to output
	 * Also initializes the text contents of each file
	 */
	private void displayFile(File file, String text) {
		System.out.println("\nFile: " + file.getAbsolutePath());
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
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            text += line + System.getProperty("line.separator"); // Adds a newline character to the end of the string
        }
        // Print the final string which represents the sum file contents
        System.out.println(text);
	}

	/*
	 * Destroys the test environment
	 */
	@After
	public void tearDown() {
		// Empty
	}

}
