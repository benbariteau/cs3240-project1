import java.io.File;

public class Test {

	public static void main(String[] args) {
		System.out.println("Running with files: "+new File("test/SampleSpec").getAbsolutePath()+","+new File("test/SampleSpec").getAbsolutePath());
		
		Main.main(new String[]{"test/SampleSpec","test/SampleInput"});
	}

}
