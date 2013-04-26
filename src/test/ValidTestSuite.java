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
	 * testcase1 - The original testcase given by the TA's
	 * testcase2 - A modified version of the original testcase - shorter length
	 * testcase3 - A modified version of the original testcase - longer length
	 * testcase4 - A modified version of the original testcase - whitespace heavy case
	 * testcase5 - A modified version of the original testcase - empty/null case
	 * 
	 * 
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "resources/testcase1/grammar.txt", "resources/testcase1/input.txt", "resources/testcase1/output.txt", "resources/testcase1/script.txt", "resources/testcase1/spec.txt"},
				{ "resources/testcase2/grammar.txt", "resources/testcase2/input.txt", "resources/testcase2/output.txt", "resources/testcase2/script.txt", "resources/testcase2/spec.txt"},
				{ "resources/testcase3/grammar.txt", "resources/testcase3/input.txt", "resources/testcase3/output.txt", "resources/testcase3/script.txt", "resources/testcase3/spec.txt"},
				{ "resources/testcase4/grammar.txt", "resources/testcase4/input.txt", "resources/testcase4/output.txt", "resources/testcase4/script.txt", "resources/testcase4/spec.txt"},
				{ "resources/testcase5/grammar.txt", "resources/testcase5/input.txt", "resources/testcase5/output.txt", "resources/testcase5/script.txt", "resources/testcase5/spec.txt"}
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
	 * Checks whether output is the same as expected output
	 */
	@Test
	public void test_all() throws IOException {
		String s = new Main().run(new String[] { this.pathGrammar, this.pathSpec, this.pathInput });
        System.out.println(s);
        assertTrue(s.equals(textOutput));
	}

}
