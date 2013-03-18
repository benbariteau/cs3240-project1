package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

	private static Map<String, String> mapClasses, mapTokens;
	private static String pathToGrammar, pathToInput;

	/*
	 * Main method and driver of the program
	 * 
	 * @arg[0] Grammar
	 * 
	 * @arg[1] Input
	 */
	public static void main(String[] args) {

		// Ensure proper input parameters
		inputValidation(args);

		// The two file paths (grammar and input file respectively)
		pathToGrammar = args[0];
		pathToInput = args[1];

		// Input and scan the grammar file
		Map<String, String>[] mapList = new Map[2];
		// Parse the classes and then the tokens
		InitParser initParse = new InitParser();
		mapList = initParse.parse(pathToGrammar, pathToInput);

		// TODO - input the input file

		// TODO - Determine parse tree

		// TODO - Create output based on input

        Rule CLS_CHAR = new Rule("CLS_CHAR");
        CLS_CHAR.addProduction(T('a'));
        //all of the CLS_CHAR characters should be productions;
        CLS_CHAR.addProduction(T('Z'));

        Rule RE_CHAR = new Rule("RE_CHAR");
        //add all of the RE_CHAR characters at productions

        List<Rule> rules = new ArrayList<Rule>();

        Rule regEx = new Rule("reg-ex");
        Rule rexp = new Rule("rexp");
        Rule rexpPrime = new Rule("rexp\'");
        Rule rexp1 = new Rule("rexp1");
        Rule rexp1Prime = new Rule("rexp1\'");
        Rule rexp2 = new Rule("rexp2");
        Rule rexp2tail = new Rule("rexp2-tail");
        Rule rexp3 = new Rule("rexp3");
        Rule charClass = new Rule("char-class");
        Rule charClass1 = new Rule("char-class-1");
        Rule charSetList = new Rule("char-set-list");
        Rule charSet = new Rule("char-set");
        Rule charSetTail = new Rule("char-set-tail");
        Rule excludeSet = new Rule("exclude-set");
        Rule excludeSetTail = new Rule("exclude-set-tail");

        regEx.addProduction(rexp);

        rexp.addProduction(rexp1, rexpPrime);

        rexpPrime.addProduction(T('|'), rexp1, rexpPrime);
        rexpPrime.addProduction(new EmptyString());

        rexp1.addProduction(rexp2, rexp1Prime);

        rexp1Prime.addProduction(rexp2, rexp1Prime);
        rexp1Prime.addProduction(new EmptyString());

        rexp2.addProduction(T('('), rexp, T(')'), rexp2tail);
        rexp2.addProduction(RE_CHAR, rexp2tail);
        rexp2.addProduction(rexp3);

        rexp2tail.addProduction(T('*'));
        rexp2tail.addProduction(T('+'));
        rexp2tail.addProduction(new EmptyString());

        rexp3.addProduction(charClass);
        rexp3.addProduction(new EmptyString());

        charClass.addProduction(T('.'));
        charClass.addProduction(T('['), charClass1);
        charClass.addProduction(new DefinedClass());

        charClass1.addProduction(charSetList);
        charClass1.addProduction(excludeSet);

        charSetList.addProduction(charSet, charSetList);
        charSetList.addProduction(new Terminal(']'));

        charSet.addProduction(CLS_CHAR, charSetTail);

        charSetTail.addProduction(new Terminal('-'), CLS_CHAR);
        charSetTail.addProduction(new EmptyString());

        excludeSet.addProduction(T('^'), charSet, T(']'), T(' '), T('I'), T('N'), T(' '), excludeSetTail);

        excludeSetTail.addProduction(new Terminal('['), charSet, new Terminal(']'));
        excludeSetTail.addProduction(new DefinedClass());
	}

    private static Terminal T(char c) {
        return new Terminal(']');
    }

    /*
     * Test inputs to ensure validity
     */
	private static void inputValidation(String[] args) {
		// Must have two argument inputs - grammar and sample input
		if (args.length != 2) {
			System.out.println("Invalid parameters. Try:\njava Main <path/to/grammar> <path/to/input>");
			System.exit(-1);
		}
	}

}
