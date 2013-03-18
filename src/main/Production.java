package main;

import java.util.Arrays;
import java.util.List;

public class Production {
    List<Symbol> symbols;

    public Production(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public Production(Symbol... symbols) {
        this.symbols = Arrays.asList(symbols);
    }
}
