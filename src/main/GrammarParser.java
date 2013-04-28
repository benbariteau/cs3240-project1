package main;

import main.grammar.DfaRule;
import main.grammar.EmptyString;
import main.grammar.Grammar;
import main.grammar.MultiToken;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Terminal;
import main.grammar.Token;
import main.parse.NamedDFA;

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

    public Grammar parse(Scanner s, List<NamedDFA> dfas) {
        while (s.hasNextLine()) {
            Rule r = parseRule(s.nextLine(), dfas);
            grammar.addRule(r);
        }
        return grammar;
    }

    private Rule parseRule(String s, List<NamedDFA> dfas) {
        Pattern p = Pattern.compile("<[^ ]+>");
        Matcher m = p.matcher(s);
        m.find();
        String ruleName = m.group(0);
        Rule r = getOrCreateRule(ruleName);

        String rest = s.substring(m.end());
        rest = removeGrammarAssignment(rest);
        List<Production> productions = getProductions(rest, dfas);
        r.addProductions(productions);

        return r;
    }

    private static Set<Character> token_id_chars = new HashSet<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z', '-', '_'));

    private List<Production> getProductions(String s, List<NamedDFA> dfas) {
        List<Production> productions = new ArrayList<Production>();

        Production production = new Production();
        while (!s.isEmpty()) {
            char c = s.charAt(0);
            int i;
            switch (c) {
                case '<':
                    if (s.length() >= 9 && s.substring(0, 9).equals("<epsilon>")) {
                        production.add(new EmptyString());
                        s = s.length() > 10 ? s.substring(10).trim() : "";
                    } else {
                        String ruleName = "";
                        for (i = 0; s.charAt(i) != '>'; i++) {
                            ruleName += s.charAt(i);
                        }
                        ruleName += '>';
                        Rule r = getOrCreateRule(ruleName);
                        production.add(r);
                        s = s.substring(i+1).trim();
                    }
                    break;
                case '|':
                    productions.add(production);
                    production = new Production();
                    s = s.substring(1).trim();
                    break;
                case 'A':
                case 'I':
                case 'R':
                    String ruleName = "$";
                    for (i = 0; i < s.length() && token_id_chars.contains(s.charAt(i)) ;i++) {
                        ruleName += s.charAt(i);
                    }
                    Token token = new Token(ruleName, "");
                    s = s.substring(i).trim();
                    production.add(token);
                    break;
                default:
                    String tokenName = null;
                    for (NamedDFA dfa : dfas) {
                        NamedDFA.Output output = dfa.run(s);
                        if (output != null) {
                            s = s.substring(output.offset).trim();
                            production.add(new Token(dfa.getName(), output.token));
                            break;
                        }
                    }
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
