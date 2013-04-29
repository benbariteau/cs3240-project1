package test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/*
 * Parameterized test cases - the set of tests run over all input parameters
 */
public abstract class TestSuite {

	// Localized file paths
	protected String pathGrammar = "", pathSpec = "", pathScript = "";

	// The actual files
	private File fileGrammar, fileScript, fileSpec;

	// The texts contained within the files
	protected String textOutput = "";
	
	/*
	 * Constructor for the run of tests
	 */
	public TestSuite(String pathGrammar, String pathScript, String pathSpec) {
		this.pathGrammar = pathGrammar;
		this.pathScript = pathScript;
		this.pathSpec = pathSpec;
	 }

	/*
	 * Setup the test environment
	 */
	@Before
	public void setUp() {
		System.out.println("\nFiles:");
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
		fileScript = new File(pathScript);
		fileSpec= new File(pathSpec);
	}

	/*
	 * Display all the file names and checks contents
	 */
	private void display() {
		System.out.println(fileGrammar.getAbsolutePath());
		System.out.println(fileScript.getAbsolutePath());
		System.out.println(fileSpec.getAbsolutePath() + "\n");
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
