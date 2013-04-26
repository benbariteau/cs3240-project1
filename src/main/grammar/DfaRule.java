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
        return dfa.getFirstSet();
    }

    public String run(List<Symbol> inputSymbols) throws Exception {
        String token = "";
        dfa.start();

        while (((Terminal)inputSymbols.get(0)).isWhitespace()) {
            inputSymbols.remove(0);
        }

        boolean done = false;
        Symbol symbol = inputSymbols.get(0);
        while (!done && !(symbol instanceof EndOfInput)) {
            char c = ((Terminal)symbol).getCharacter();
            done = dfa.next(c);
            token += c;
            inputSymbols.remove(0);
            symbol = inputSymbols.get(0);
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
