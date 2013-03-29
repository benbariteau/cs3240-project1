package test;

import static org.junit.Assert.assertFalse;

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
public class InvalidTestSuite extends TestSuite {

	/*
	 * The following test cases are invalid and should fail:
	 * set0 - Ambiguous grammar
	 * set1 -
	 * set2 -
	 * set3 -
	 * set4 -
	 * set5 -
	 * set6 -
	 * set7 -
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
	public void test_all() throws IOException {
		String s = new Main().run(new String[] { this.pathGrammar, this.pathInput });
        System.out.println(s);
        assertFalse(s.equals(this.textOutput));
	}

}
