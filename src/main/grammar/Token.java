package main.grammar;

import java.util.Set;

public class Token implements Symbol {
    String name;

    public Token(String name) {
        this.name = name;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        return null;
    }

    public String toString() {
        return name;
    }
}
