package main;

import main.exception.UnrecognizedTokenException;
import main.grammar.DfaRule;
import main.grammar.EmptyString;
import main.grammar.Grammar;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarParser {
    Grammar grammar;
    Map<String, Rule> nameRuleMap;

    public GrammarParser() {
        grammar = new Grammar();
        nameRuleMap = new HashMap<String, Rule>();
    }

    public Grammar parse(Scanner s, Map<String, DfaRule> dfaRules, LabelledDFA tokenRecognizer) throws UnrecognizedTokenException {
        while (s.hasNextLine()) {
            Rule r = parseRule(s.nextLine(), dfaRules, tokenRecognizer);
            grammar.addRule(r);
        }
        return grammar;
    }

    private Rule parseRule(String s, Map<String, DfaRule> dfaRules, LabelledDFA tokenRecognizer) throws UnrecognizedTokenException {
        Pattern p = Pattern.compile("<[^ ]+>");
        Matcher m = p.matcher(s);
        m.find();
        String ruleName = m.group(0);
        Rule r = getOrCreateRule(ruleName);

        String rest = s.substring(m.end());
        rest = removeGrammarAssignment(rest);
        List<Production> productions = getProductions(rest, dfaRules, tokenRecognizer);
        r.addProductions(productions);

        return r;
    }

    private static Set<Character> token_id_chars = new HashSet<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z', '-', '_'));

    private List<Production> getProductions(String s, Map<String, DfaRule> dfaRules, LabelledDFA tokenRecognizer) throws UnrecognizedTokenException {
        List<Production> productions = new ArrayList<Production>();

        Production production = new Production();
        int i;
        while (!s.isEmpty()) {
            char c = s.charAt(0);
            switch (c) {
                case '<':
                    if (s.length() >= 9 && s.substring(0, 9).equals("<epsilon>")) {
                        production.add(new EmptyString());
                        s = s.substring(9).trim();
                    } else {
                        String ruleName = "";
                        for (i = 0; s.charAt(i) != '>'; i++) {
                            ruleName += s.charAt(i);
                        }
                        ruleName += '>';
                        Rule r = getOrCreateRule(ruleName);
                        s = s.substring(i+1).trim();
                        production.add(r);
                    }
                    break;
                case '|':
                    productions.add(production);
                    production = new Production();
                    s = s.substring(1).trim();
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    String ruleName = "";
                    for (i = 0; i < s.length() && token_id_chars.contains(s.charAt(i)); i++) {
                        ruleName += s.charAt(i);
                    }
                    Token t = new Token(ruleName, "");
                    production.add(t);
                    s = s.substring(i).trim();
                    break;
                default:
                    String tokenString = tokenRecognizer.run(s);
                    Token token = new Token(tokenRecognizer.getLastToken(), tokenString);
                    production.add(token);
                    s = s.substring(tokenString.length()).trim();
                    break;
            }
        }
        productions.add(production);
        return productions;
    }

    private Rule getOrCreateRule(String ruleName) {
        Rule r = nameRuleMap.get(ruleName);
        if (r == null) {
            r = new Rule(ruleName);
            nameRuleMap.put(ruleName, r);
        }
        return r;
    }

    private String removeGrammarAssignment(String s) {
        Pattern p = Pattern.compile("\\s*::=\\s*");
        Matcher m = p.matcher(s);
        m.find();
        return s.substring(m.end());
    }

}
