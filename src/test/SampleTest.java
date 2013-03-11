package test;

import static org.junit.Assert.assertTrue;

import java.io.File;

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

	@Test
	public void test_all() {
		Main.main(new String[] { pathGrammar, pathInput });
		assertTrue(true);
	}

	@Before
	public void setUp() {
		System.out.println("Running with sample files:");
		System.out.println(new File(pathGrammar).getAbsolutePath());
		System.out.println(new File(pathInput).getAbsolutePath());
		System.out.println(new File(pathOutput).getAbsolutePath());
	}

	@After
	public void tearDown() {
		// Empty
	}

}
