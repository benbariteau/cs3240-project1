package main;

import main.exception.UnrecognizedTokenException;
import main.exception.UnexpectedSymbolException;
import main.grammar.EmptyString;
import main.grammar.Grammar;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Terminal;
import main.grammar.Token;
import main.parse.ParseNode;
import main.parse.ParseTable;
import main.parse.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	private Map<String, String> mapClasses, mapTokens;
	private String pathToGrammar, pathToTokenSpec, pathToInput;
    static Set<Character> printableAscii = new HashSet<Character>(Arrays.asList(' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'));
    Map<String, NFA> nfas = new HashMap<String, NFA>();
    NFA bigNFA;
    DFA dfa;
    Map<String, ParseTree> classesParseTrees =  new HashMap<String, ParseTree>();
    Map<String, ParseTree> tokensParseTrees = new HashMap<String, ParseTree>();

    public static void main(String[] args) throws IOException {
        try {
            System.out.println(new Main().run(args));
        } catch (UnexpectedSymbolException e) {
            e.printMessage(System.out);
        } catch (UnrecognizedTokenException e) {
            e.printMessage(System.out);
        }
    }

	/*
	 * Main method and driver of the program
	 * 
	 * @arg[0] Grammar
	 * 
	 * @arg[1] Input
	 */
	public String run(String[] args) throws IOException, UnexpectedSymbolException, UnrecognizedTokenException {

		// Ensure proper input parameters
		inputValidation(args);

		// The two file paths (grammar and input file respectively)
		pathToGrammar = args[0];
        pathToTokenSpec = args[1];
		pathToInput = args[2];

		// Parse the classes and then the tokens
		TokenParser initParse = new TokenParser();
		TokenParser.TokenInfo info = initParse.parse(pathToTokenSpec, pathToInput);

        Map<String, String> characterClasses = info.characterClasses;
        Map<String, String> tokens = info.tokens;

        Grammar regexRules = createRegexRules();
        ParseTable parseTable = regexRules.createParseTable();

        for(String key : characterClasses.keySet()) {
            ParseTree parseTree = parseTable.parse(characterClasses.get(key), regexRules.getStartRule());
            classesParseTrees.put(key, parseTree);
        }

        for(String key : tokens.keySet()) {
            ParseTree parseTree = parseTable.parse(tokens.get(key), regexRules.getStartRule());
            tokensParseTrees.put(key, parseTree);
        }

        //Create basic NFAs for each class and token
        createNFAs(classesParseTrees);
        createNFAs(tokensParseTrees);

        Map<NFA, String> tokenNFAs = new HashMap<NFA, String>();
        for (String token : tokensParseTrees.keySet()) {
            tokenNFAs.put(nfas.get(token), token.substring(1));
        }
        LabelledDFA ldfa = LabelledDFA.createFromNFAs(tokenNFAs);
        ldfa.setTokenPriority(info.tokenList);

        File inputFile = new File(pathToInput);

        Reader r = new InputStreamReader(new FileInputStream(inputFile), Charset.defaultCharset());

        List<Token> inputTokens = parseInput(ldfa, r);
        for (Token token : inputTokens) {
            System.out.println(token.toLongString());
        }

        Grammar grammar = new GrammarParser().parse(new Scanner(new File(pathToGrammar)), null, ldfa);
        System.out.println(grammar);

        ParseTree tree = grammar.createParseTable().parse(inputTokens, grammar.getStartRule());
        System.out.println(tree);

        return "";
	}

    private List<Token> parseInput(LabelledDFA ldfa, Reader r) throws IOException, UnrecognizedTokenException {
        List<Token> tokens = new ArrayList<Token>();
        String buffer = "";
        int val = r.read();
        while (val != -1) {
            char c = (char) val;

            Set<Integer> statuses = null;
            try {
                statuses = ldfa.next(c);
            } catch (UnrecognizedTokenException e) {
                throw new UnrecognizedTokenException(buffer, buffer.length()-1);
            }

            if (statuses.contains(LabelledDFA.TOKEN_END)) {
                tokens.add(new Token(ldfa.getLastToken(), buffer));
                buffer = "";
            }

            if (statuses.contains(LabelledDFA.REGALAR)) {
                buffer += c;
            }

            val = r.read();
        }
        return tokens;
    }

    private void combineNFAs()
	{
		bigNFA = NFA.unionNFAs(nfas.values());
	}

    private void createNFAs(Map<String, ParseTree> parseTrees) {
        for (String key : parseTrees.keySet()) {
            ParseTree parseTree = parseTrees.get(key);
            if (nfas.get(key) == null) {
                NFA nfa = createNFA(parseTree);
                nfas.put(key, nfa);
            }
//            System.out.println(key + " " + parseTree.getInputString());
//            System.out.println(nfas.get(key));
//            System.out.println(DFA.createFromNFA(nfas.get(key)) + "\n");
        }
    }

    private NFA createNFA(ParseTree parseTree) {
        return parseRexp(parseTree.getHead().getChildren().get(0));
    }

    private NFA parseRexp(ParseNode rexp) {
        return getUnionList(rexp);
    }

    private NFA getUnionList(ParseNode rexp) {
        List<ParseNode> nodes = rexp.getChildren();
        ParseNode rexp1 = nodes.get(0);
        ParseNode rexpPrime = nodes.get(1);
        NFA contactList = getConcatList(rexp1);
        if (!rexpPrime.hasEmptyChildren()) {
            NFA unionRHS = getUnionRHS(rexpPrime);
            return contactList.unionNFA(unionRHS);
        }
        return contactList;
    }

    private NFA getUnionRHS(ParseNode rexpPrime) {
        List<ParseNode> nodes = rexpPrime.getChildren();
        ParseNode rexp1 = nodes.get(1);
        ParseNode rexpPrime2 = nodes.get(2);
        NFA concatList = getConcatList(rexp1);
        if (!rexpPrime2.hasEmptyChildren()) {
            NFA unionRHS = getUnionRHS(rexpPrime2);
            return concatList.unionNFA(unionRHS);
        }
        return concatList;
    }

    private NFA getConcatList(ParseNode rexp1) {
        List<ParseNode> nodes = rexp1.getChildren();
        ParseNode rexp2 = nodes.get(0);
        ParseNode rexp1Prime = nodes.get(1);
        NFA basicNFA = getBasicRegex(rexp2);
        if(!rexp1Prime.hasEmptyChildren()) {
            NFA concatList = getConcatList(rexp1Prime);
            return basicNFA.concatNFA(concatList);
        }
        return basicNFA;
    }

    private NFA getBasicRegex(ParseNode rexp2) {
        List<ParseNode> nodes = rexp2.getChildren();
        ParseNode firstNode = nodes.get(0);
        if (firstNode.getSymbol().equals(T('('))) {
            ParseNode rexp = nodes.get(1);
            NFA unionList = getUnionList(rexp);
            String rexp2tail = nodes.get(3).getInputString();
            return createTailedNFA(unionList, rexp2tail);
        } else if (firstNode.getSymbol() instanceof Rule && ((Rule) firstNode.getSymbol()).getName().equals("RE_CHAR")) {
            NFA singleCharNFA = null;
            if (firstNode.getChildren().size() == 1) {
                singleCharNFA = NFA.constructNFAFromCharacter(((Terminal)firstNode.getChildren().get(0).getSymbol()).getCharacter());
            } else {
                singleCharNFA = NFA.constructNFAFromCharacter(((Terminal)firstNode.getChildren().get(1).getChildren().get(0).getSymbol()).getCharacter());
            }
            String rexp2tail = nodes.get(1).getInputString();
            return createTailedNFA(singleCharNFA, rexp2tail);
        } else if (firstNode.getSymbol() instanceof Rule && ((Rule) firstNode.getSymbol()).getName().equals("rexp3")) {
            if(!firstNode.hasEmptyChildren()) {
                return getCharClass(firstNode.getChildren().get(0));
            }
        }
        return new NFA();
    }

    private NFA createTailedNFA(NFA nfa, String rexp2tail) {
        if (rexp2tail.equals("*")) {
            return nfa.createStarNFA();
        } else if(rexp2tail.equals("+")) {
            return nfa.createPlusNFA();
        }
        return nfa;
    }

    private NFA getCharClass(ParseNode charClass) {
        List<ParseNode> nodes = charClass.getChildren();
        ParseNode firstNode = nodes.get(0);
        if (firstNode.getSymbol().equals(T('.'))) {
            return NFA.constructNFAFromCharacterSet(printableAscii);
        } else if (firstNode.getSymbol().equals(T('['))) {
            return getCharClass1(nodes.get(1));
        } else {
            return getDefinedClass(firstNode);
        }
    }

    private NFA getDefinedClass(ParseNode definedClass) {
        String classId = definedClass.getInputString();
        return getNFA(classId);
    }

    private NFA getNFA(String classId) {
        NFA nfa = nfas.get(classId);
        if (nfa == null) {
            ParseTree classTree = classesParseTrees.get(classId);
            ParseTree tokenTree = tokensParseTrees.get(classId);
            if(classTree != null) {
                nfas.put(classId, parseRexp(classTree.getHead().getChildren().get(0)));
            }

            if (tokenTree != null) {
                nfas.put(classId, parseRexp(tokenTree.getHead().getChildren().get(0)));
            }
        }
        return nfas.get(classId).clone();
    }

    private NFA getCharClass1(ParseNode charClass1) {
        ParseNode node = charClass1.getChildren().get(0);
        Rule rule = (Rule) node.getSymbol();
        if (rule.getName().equals("char-set-list")) {
            return NFA.constructNFAFromCharacterSet(getCharSetList(node));
        } else {
            return getExcludeSet(node);
        }
    }

    private NFA getExcludeSet(ParseNode excludeSet) {
        List<ParseNode> nodes = excludeSet.getChildren();
        Set<Character> charSet = getCharSet(nodes.get(1));
        NFA excludeSetTail = getExcludeSetTail(nodes.get(7));
        return excludeSetTail.excludeCharacters(charSet);
    }

    private NFA getExcludeSetTail(ParseNode excludeSetTail) {
        List<ParseNode> nodes = excludeSetTail.getChildren();
        ParseNode firstNode = nodes.get(0);
        if (firstNode.getSymbol().equals(T('['))) {
            return NFA.constructNFAFromCharacterSet(getCharSet(nodes.get(1)));
        } else {
            return getDefinedClass(firstNode);
        }
    }

    private Set<Character> getCharSetList(ParseNode charSetList) {
        List<ParseNode> nodes = charSetList.getChildren();
        ParseNode firstNode = nodes.get(0);
        if(firstNode.getSymbol().equals(T(']'))) {
            return new HashSet<Character>();
        } else {
            Set<Character> charSet = getCharSet(firstNode);
            Set<Character> charsSet = getCharSetList(nodes.get(1));
            charsSet.addAll(charSet);
            return charsSet;
        }
    }

    private Set<Character> getCharSet(ParseNode charSet) {
        Set<Character> characters = new HashSet<Character>();

        List<ParseNode> nodes = charSet.getChildren();
        ParseNode CLS_CHAR = nodes.get(0);
        ParseNode charSetTail = nodes.get(1);
        Terminal t = (Terminal) CLS_CHAR.getChildren().get(0).getSymbol();
        char start = t.getCharacter();
        if(charSetTail.hasEmptyChildren()) {
            characters.add(start);
        } else {
            Terminal e = (Terminal) charSetTail.getChildren().get(1).getChildren().get(0).getSymbol();
            char end = e.getCharacter();
            for(char i = start; i <= end; i++) {
                characters.add(i);
            }
        }
        return characters;
    }

    private Grammar createRegexRules() {
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
        Rule definedClass = new Rule("defined-class");
        Rule definedClass1 = new Rule("defined-class1");
        Rule CLS_CHAR = new Rule("CLS_CHAR");
        Rule CLS_CHAR_ESCAPE = new Rule("CLS_CHAR_ESCAPE");
        Rule RE_CHAR = new Rule("RE_CHAR");
        Rule RE_CHAR_ESCAPE = new Rule("RE_CHAR_ESCAPE");

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

        definedClass.addProduction(T('$'), definedClass1);

        definedClass1.addProduction(RE_CHAR, definedClass1);
        definedClass1.addProduction(new EmptyString());

        Set<Character> escapeCharacters = new HashSet<Character>(Arrays.asList('\\', '^', '-', '[', ']'));
        CLS_CHAR.addProductions(getCharList(printableAscii, escapeCharacters));
        CLS_CHAR.addProduction(T('\\'), CLS_CHAR_ESCAPE);

        CLS_CHAR_ESCAPE.addProductions(getCharList(escapeCharacters));

        escapeCharacters = new HashSet<Character>(Arrays.asList('\\', ' ', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"', '$'));
        RE_CHAR.addProductions(getCharList(printableAscii, escapeCharacters));
        RE_CHAR.addProduction(T('\\'), RE_CHAR_ESCAPE);

        RE_CHAR_ESCAPE.addProductions(getCharList(escapeCharacters));

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
        rules.add(definedClass);
        rules.add(definedClass1);
        rules.add(CLS_CHAR);
        rules.add(CLS_CHAR_ESCAPE);
        rules.add(RE_CHAR);
        rules.add(RE_CHAR_ESCAPE);

        return new Grammar(regEx, rules);
    }

    private List<Production> getCharList(Set<Character> escapeCharacters) {
        List<Production> productions = new ArrayList<Production>();
        for (char c : escapeCharacters) {
            productions.add(new Production(T(c)));
        }
        return productions;
    }

    private List<Production> getCharList(Set<Character> charSet, Set<Character> escapedCharacters) {
        List<Production> charList = new ArrayList<Production>();
        for(char c : charSet) {
            if (escapedCharacters.contains(c)) {
                continue;
            }
            charList.add(new Production(T(c)));
        }
        return charList;
    }

    private Terminal T(char c) {
        return new Terminal(c);
    }

    /*
     * Test inputs to ensure validity
     */
	private void inputValidation(String[] args) {
		// Must have two argument inputs - grammar and sample input
		if (args.length != 3) {
			System.out.println("Invalid parameters. Try:\njava Main <path/to/grammar> <path/to/token/spec> <path/to/input>");
			System.exit(-1);
		}
	}

}
