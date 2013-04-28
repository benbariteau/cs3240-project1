package main.grammar;

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
        return null;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!name.equals(token.name)) return false;
        if (value != null ? !value.equals(token.value) : token.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
