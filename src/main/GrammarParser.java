package main;

import main.grammar.DfaRule;
import main.grammar.EmptyString;
import main.grammar.Grammar;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Terminal;

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

    public Grammar parse(Scanner s, Map<String, DfaRule> dfaRules) {
        while (s.hasNextLine()) {
            Rule r = parseRule(s.nextLine(), dfaRules);
            grammar.addRule(r);
        }
        return grammar;
    }

    private Rule parseRule(String s, Map<String, DfaRule> dfaRules) {
        Pattern p = Pattern.compile("<[^ ]+>");
        Matcher m = p.matcher(s);
        m.find();
        String ruleName = m.group(0);
        Rule r = getOrCreateRule(ruleName);
        System.out.println(r.getName());

        String rest = s.substring(m.end());
        rest = removeGrammarAssignment(rest);
        List<Production> productions = getProductions(rest, dfaRules);
        r.addProductions(productions);

        return r;
    }

    private static Set<Character> token_id_chars = new HashSet<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z', '-', '_'));

    private List<Production> getProductions(String s, Map<String, DfaRule> dfaRules) {
        List<Production> productions = new ArrayList<Production>();
        char[] productionListChars = s.toCharArray();

        Production production = new Production();
        for (int i = 0; i < productionListChars.length; i++) {
            switch (productionListChars[i]) {
                case '<':
                    if (s.length() >= i+9 && s.substring(i, i+9).equals("<epsilon>")) {
                        production.add(new EmptyString());
                        i +=10;
                    } else {
                        String ruleName = "";
                        for (; productionListChars[i] != '>'; i++) {
                            ruleName += productionListChars[i];
                        }
                        ruleName += '>';
                        Rule r = getOrCreateRule(ruleName);
                        production.add(r);
                    }
                    break;
                case '|':
                    productions.add(production);
                    production = new Production();
                    break;
                case ' ':
                    break;
                case 'A':
                case 'I':
                case 'R':
                    String ruleName = "$";
                    for (; i < productionListChars.length && token_id_chars.contains(productionListChars[i]); i++) {
                        ruleName += productionListChars[i];
                    }
                    DfaRule r = dfaRules.get(ruleName);
                    production.add(r);
                    break;
                default:
                    production.add(new Terminal(productionListChars[i]));
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
