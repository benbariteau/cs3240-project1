package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        Rule definedClass = new Rule("defined-class");
        for(String key : mapList[0].keySet()) {
            definedClass.addProduction(key);
        }

        List<Rule> regexRules = createRegexRules(definedClass);
        Map<Rule, Map<Symbol, Production>> parseTable = createParseTable(regexRules);

        for(Rule rule : regexRules) {
            Map<Symbol, Production> productionMap = parseTable.get(rule);
            System.out.println(rule.getName()+":");
            for(Symbol symbol : productionMap.keySet()) {
                System.out.println("\t{ "+symbol+" => "+productionMap.get(symbol)+" }");
            }
        }

		// TODO - input the input file

		// TODO - Determine parse tree

		// TODO - Create output based on input
	}

    private static Map<Rule, Map<Symbol, Production>> createParseTable(List<Rule> rules) {
        Map<Rule, Map<Symbol, Production>> table = new HashMap<Rule, Map<Symbol, Production>>();
        Map<Rule, Set<Symbol>> followSetMap = new HashMap<Rule, Set<Symbol>>();

        for (Rule rule : rules) {
            followSetMap.put(rule, new HashSet<Symbol>());
        }

        //Find follow for each rule
        followSetMap.get(rules.get(0)).add(new EndOfInput());
        boolean changed = true;
        while(changed) {
            changed = false;
            for (Rule rule : rules) {
                for (Production p : rule.getProductions()) {
                    List<Symbol> symbolList = p.getSymbolList();
                    for (int i = 0; i < symbolList.size(); i++) {
                        Symbol symbol = symbolList.get(i);
                        if (rules.contains(symbol)) {
                            Set<Symbol> followFirst = new Production(symbolList.subList(i+1, symbolList.size())).getFirstSet(rule);
                            boolean containedEmptyString = followFirst.remove(new EmptyString());
                            changed = changed || followSetMap.get(symbol).addAll(followFirst);
                            if (containedEmptyString) {
                                changed = changed || followSetMap.get(symbol).addAll(followSetMap.get(rule));
                            }
                        }
                    }
                }
            }
        }

        //Construct parse table
        for(Rule rule : rules) {
            Map<Symbol, Production> productionMap = new HashMap<Symbol, Production>();
            Set<Symbol> followSet = followSetMap.get(rule);
            for (Production p : rule.getProductions()) {
                Set<Symbol> productionFirst = p.getFirstSet(rule);
                for(Symbol s : productionFirst) {
                    if (s instanceof Terminal) {
                        productionMap.put(s, p);
                    } else if (s instanceof EmptyString) {
                        for(Symbol sym : followSet) {
                            if (productionMap.get(sym) == null) {
                                productionMap.put(sym, p);
                            }
                        }
                    }
                }
            }
            table.put(rule, productionMap);
        }

        return table;
    }

    public static void printSymbolSet(Set<Symbol> symbolSet) {
        System.out.print("{");
        for(Symbol s : symbolSet) {
            System.out.print(s + ", ");
        }
        System.out.println("}");
    }

    private static List<Rule> createRegexRules(Rule definedClass) {
        char[] printableAscii = {' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C',
                'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'};

        Set<Character> escapeCharacters = new HashSet<Character>(Arrays.asList('^', '-', '[', ']'));
        Rule CLS_CHAR = new Rule("CLS_CHAR", getCharList(printableAscii, '\\', escapeCharacters));

        escapeCharacters.addAll(Arrays.asList(' ', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"'));
        Rule RE_CHAR = new Rule("RE_CHAR", getCharList(printableAscii, '\\', escapeCharacters));

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
        charClass.addProduction(definedClass);

        charClass1.addProduction(charSetList);
        charClass1.addProduction(excludeSet);

        charSetList.addProduction(charSet, charSetList);
        charSetList.addProduction(new Terminal(']'));

        charSet.addProduction(CLS_CHAR, charSetTail);

        charSetTail.addProduction(new Terminal('-'), CLS_CHAR);
        charSetTail.addProduction(new EmptyString());

        excludeSet.addProduction(T('^'), charSet, T(']'), T(' '), T('I'), T('N'), T(' '), excludeSetTail);

        excludeSetTail.addProduction(new Terminal('['), charSet, new Terminal(']'));
        excludeSetTail.addProduction(definedClass);

        List<Rule> rules = new ArrayList<Rule>();
        rules.add(regEx);
        rules.add(rexp);
        rules.add(rexpPrime);
        rules.add(rexp1);
        rules.add(rexp1Prime);
        rules.add(rexp2);
        rules.add(rexp2tail);
        rules.add(rexp3);
        rules.add(charClass);
        rules.add(charClass1);
        rules.add(charSetList);
        rules.add(charSet);
        rules.add(charSetTail);
        rules.add(excludeSet);
        rules.add(excludeSetTail);

        return rules;
    }

    private static List<Production> getCharList(char[] charSet, char escapeChar, Set<Character> escapedCharacters) {
        List<Production> charList = new ArrayList<Production>();
        charList.add(new Production(T(escapeChar), T(escapeChar)));
        for(char c : charSet) {
            if (escapedCharacters.contains(c)) {
                charList.add(new Production(T(escapeChar), T(c)));
                continue;
            }
            charList.add(new Production(T(c)));
        }
        return charList;
    }

    private static Terminal T(char c) {
        return new Terminal(c);
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
