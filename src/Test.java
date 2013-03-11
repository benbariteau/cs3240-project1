import java.io.File;

public class Test {

	/*
	 * Test on the samples given out on T-Square
	 * The samples are packaged in the project directory
	 */
	public static void main(String[] args) {
		System.out.println("Running with files:\n" + 
							new File("test/SampleSpec").getAbsolutePath() + "\n" +
							new File("test/SampleSpec").getAbsolutePath() + "\n");

		Main.main(new String[] { "test/SampleSpec", "test/SampleInput" });
	}

}
