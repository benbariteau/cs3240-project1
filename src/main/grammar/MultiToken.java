package main.grammar;

import java.util.HashSet;
import java.util.Set;

public class MultiToken implements Symbol {
    Set<Token> names;
    String value;

    public MultiToken(String name, String value) {
        names = new HashSet<Token>();
        names.add(new Token(name, ""));
        this.value = value;
    }

    public MultiToken(Set<String> names, String value) {
        this.names = new HashSet<Token>();
        for (String name : names) {
            this.names.add(new Token(name, ""));
        }
        this.value = value;
    }

    public String toString() {
        return names.toString();
    }

    @Override
    public Set<Symbol> getFirstSet() {
        Set<Symbol> first = new HashSet<Symbol>();
        for (Token t : names) {
            first.add(t);
        }
        return first;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiToken token = (MultiToken) o;

        if (!names.equals(token.names)) return false;
        if (value != null ? !value.equals(token.value) : token.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = names.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public Set<Token> getTokens() {
        return names;
    }
}
