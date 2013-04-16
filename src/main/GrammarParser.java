package main;

import main.grammar.EmptyString;
import main.grammar.Grammar;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarParser {
    Grammar grammar;
    Map<String, Rule> nameRuleMap;

    public GrammarParser() {
        grammar = new Grammar();
        nameRuleMap = new HashMap<String, Rule>();
    }

    public Grammar parse(Scanner s) {
        while (s.hasNextLine()) {
            Rule r = parseRule(s.nextLine());
            grammar.addRule(r);
        }
        return grammar;
    }

    private Rule parseRule(String s) {
        Pattern p = Pattern.compile("<[^ ]+>");
        Matcher m = p.matcher(s);
        m.find();
        String ruleName = m.group(0);
        Rule r = getOrCreateRule(ruleName);

        String rest = s.substring(m.end());
        rest = removeGrammarAssignment(rest);
        List<Production> productions = getProductions(rest);
        r.addProductions(productions);

        return r;
    }

    private List<Production> getProductions(String s) {
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
