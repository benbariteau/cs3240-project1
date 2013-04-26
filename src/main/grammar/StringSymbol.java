package main.grammar;

import java.util.Set;

public class StringSymbol implements Symbol {
    String string;

    public StringSymbol(String string) {
        this.string = string;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        return null;
    }

    public String toString() {
        return string;
    }
}
