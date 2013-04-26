package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import main.Main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * Parameterized test cases - the set of tests run over all input parameters
 */
@RunWith(value = Parameterized.class)
public class ValidTestSuite extends TestSuite {

	/*
	 * The list of valid test cases to use for the tests:
	 * set0 - Simple grammar and a simple input
	 * set1 - Simple grammar but a complex input
	 * set2 - Complex grammar but a simple input
	 * set3 - Complex grammar and a complex input (T-Square sample)
	 * set4 - Grammar with a lot of comments
	 * set5 - Grammar with a comment line instead of a blank line separator
	 * set6 - No unnecessary whitespace characters
	 * set7 - A lot of unnecessary whitespace
	 * 
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "resources/testcase1/grammar.txt", "resources/testcase1/input1.txt", "resources/testcase1/output1.txt", "resources/testcase1/script.txt", "resources/testcase1/spec.txt"}
		};
		return Arrays.asList(data);
	}

	/*
	 * Constructor for the run of tests
	 */
	public ValidTestSuite(String pathGrammar, String pathInput, String pathOutput, String pathScript, String pathSpec) {
		super(pathGrammar, pathInput, pathOutput, pathScript, pathSpec);
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
		String s = new Main().run(new String[] { this.pathGrammar, this.pathSpec, this.pathInput });
        System.out.println(s);
        assertTrue(s.equals(textOutput));
	}

}
