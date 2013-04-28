package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import main.Main;

import main.exception.UnrecognizedTokenException;
import main.exception.UnexpectedSymbolException;
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
				{ "resources/valid/set0/SampleGrammar", "resources/valid/set0/SampleInput", "resources/valid/set0/SampleOutput" },
				{ "resources/valid/set1/SampleGrammar", "resources/valid/set1/SampleInput", "resources/valid/set1/SampleOutput" },
				{ "resources/valid/set2/SampleGrammar", "resources/valid/set2/SampleInput", "resources/valid/set2/SampleOutput" },
				{ "resources/valid/set3/SampleGrammar", "resources/valid/set3/SampleInput", "resources/valid/set3/SampleOutput" },
				{ "resources/valid/set4/SampleGrammar", "resources/valid/set4/SampleInput", "resources/valid/set4/SampleOutput" },
				{ "resources/valid/set5/SampleGrammar", "resources/valid/set5/SampleInput", "resources/valid/set5/SampleOutput" },
				{ "resources/valid/set6/SampleGrammar", "resources/valid/set6/SampleInput", "resources/valid/set6/SampleOutput" },
				{ "resources/valid/set7/SampleGrammar", "resources/valid/set7/SampleInput", "resources/valid/set7/SampleOutput" }
		};
		return Arrays.asList(data);
	}

	/*
	 * Constructor for the run of tests
	 */
	public ValidTestSuite(String pathGrammar, String pathInput, String pathOutput) {
		super(pathGrammar, pathInput, pathOutput);
	 }

	/*
	 * 
	 * TEST CASES
	 */

	/*
	 * A single correct run
	 */
	@Test
	public void test_all() throws IOException, UnexpectedSymbolException, UnrecognizedTokenException {
		String s = new Main().run(new String[] { this.pathGrammar, this.pathInput });
        System.out.println(s);
        assertTrue(s.equals(textOutput));
	}

}
