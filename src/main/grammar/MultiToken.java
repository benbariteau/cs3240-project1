package main.grammar;

import java.util.Set;

public class MultiToken implements Symbol {
    Set<String> names;
    String value;

    public MultiToken(Set<String> names, String value) {
        this.names = names;
        this.value = value;
    }

    public String toString() {
        return names + " " + value;
    }

    @Override
    public Set<Symbol> getFirstSet() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
