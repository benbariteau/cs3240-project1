package main.grammar;

import main.parse.ParseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that represents a grammar
 */
public class Grammar {
    Rule startRule;
    List<Rule> rules;

    public Grammar() {
        startRule = null;
        rules = new ArrayList<Rule>();
    }

    /**
     * Constructor for the grammar
     */
    public Grammar(Rule startRule, List<Rule> rules) {
        this.startRule = startRule;
        this.rules = rules;

        // If the start rule isn't in rules, add it to the start of the rules.
        if (!this.rules.contains(this.startRule)) {
            this.rules.add(0, this.startRule);
        }
    }

    /**
     * Returns the start rule of the grammar
     */
    public Rule getStartRule() {
        return startRule;
    }

    /**
     * Builds a parse table from the grammar
     */
    public ParseTable createParseTable() {
        Map<Rule, Map<Symbol, Production>> table = new HashMap<Rule, Map<Symbol, Production>>();
        Map<Rule, Set<Symbol>> followSetMap = new HashMap<Rule, Set<Symbol>>();

        for (Rule rule : rules) {
            followSetMap.put(rule, new HashSet<Symbol>());
        }

        //Find follow for each rule
        followSetMap.get(startRule).add(new EndOfInput());
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
                    if (s instanceof EmptyString) {
                        for(Symbol sym : followSet) {
                            if (productionMap.get(sym) == null) {
                                productionMap.put(sym, p);
                            }
                        }
                    } else {
                        productionMap.put(s, p);
                    }
                }
            }
            table.put(rule, productionMap);
        }

        return new ParseTable(table);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule r) {
        if (rules.size() == 0) {
            startRule = r;
        }

        if (!rules.contains(r)) {
            rules.add(r);
        }
    }

    public String toString() {
        String s = "";
        for (Rule r : rules) {
            s += (r == startRule ? ">" : "") + r.toLongString() + "\n";
        }
        return s;
    }
}