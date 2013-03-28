package main.grammar;

import main.parse.ParseTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grammar {
    Rule startRule;
    List<Rule> rules;

    public Grammar(Rule startRule, List<Rule> rules) {
        this.startRule = startRule;
        this.rules = rules;

        // If the start rule isn't in rules, add it to the start of the rules.
        if (!this.rules.contains(this.startRule)) {
            this.rules.add(0, this.startRule);
        }
    }

    public Rule getStartRule() {
        return startRule;
    }

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

        return new ParseTable(table);
    }
}
