package main;

import java.util.HashSet;
import java.util.Set;

public class Terminal implements Symbol {
    private char character;

    public Terminal(char character) {
        this.character = character;
    }

    public int hashCode() {
        return character;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Terminal)) {
            return false;
        }

        return character == ((Terminal)obj).character;
    }

    @Override
    public String toString() {
        return "" + character;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        Set<Symbol> firstSet = new HashSet<Symbol>();
        firstSet.add(this);
        return firstSet;
    }
}
