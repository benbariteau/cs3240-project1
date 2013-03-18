package main;

import java.util.ArrayList;
import java.util.List;

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

}
