package main.grammar;

import java.util.HashSet;
import java.util.Set;

public class Token implements Symbol {
    String name;
    String value;

    public Token(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        Set<Symbol> first = new HashSet<Symbol>();
        first.add(this);
        return first;
    }

    public String toString() {
        return name;
    }

    public String toLongString() {
        return name + "( " + value + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!name.equals(token.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
