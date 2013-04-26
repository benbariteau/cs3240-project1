package main.grammar;

import main.DFA;

import java.util.List;
import java.util.Set;

public class DfaRule implements Symbol {
    String name;
    DFA dfa;

    public DfaRule(String name, DFA dfa) {
        this.name = name;
        this.dfa = dfa;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        return null;
    }

    public String run(List<Symbol> inputSymbols) throws Exception {
        String token = "";
        dfa.start();
        Terminal symbol = (Terminal) inputSymbols.get(0);
        while (!symbol.equals(" ")) {
            char c = symbol.getCharacter();
            dfa.next(c);
            token += c;
            inputSymbols.remove(0);
            symbol = (Terminal) inputSymbols.get(0);
        }
        boolean accept = dfa.isAccept();
        if (!accept) {
            throw new Exception("Invalid token");
        }
        return token;
    }

    public String toString() {
        return name;
    }
}
