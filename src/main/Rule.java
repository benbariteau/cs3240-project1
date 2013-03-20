package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule implements Symbol{
    String ruleName;
    List<Production> productions;

    public Rule(String ruleName) {
        this.ruleName = ruleName;
        this.productions = new ArrayList<Production>();
    }

    public Rule(String ruleName, Production production) {
        this.ruleName = ruleName;
        productions = new ArrayList<Production>();
        productions.add(production);
    }

    public Rule(String ruleName, List<Production> productions) {
        this.ruleName = ruleName;
        this.productions = productions;
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

    public void addProduction(Symbol... symbols) {
        productions.add(new Production(symbols));
    }

    public void addProduction(String string) {
        productions.add(new Production(string));
    }

    public List<Production> getProductions() {
        return productions;
    }

    public Set<Symbol> getFirstSet() {
        Set<Symbol> firstSet = new HashSet<Symbol>();
        for (Production production : productions) {
            firstSet.addAll(production.getFirstSet(this));
        }
        return firstSet;
    }

    public String getName() {
        return ruleName;
    }

    @Override
    public String toString() {
        return "<"+ruleName+">";
    }
}
