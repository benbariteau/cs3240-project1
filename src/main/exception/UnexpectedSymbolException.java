package main.exception;

import main.grammar.Symbol;
import main.grammar.Token;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class UnexpectedSymbolException extends Exception {
    private Set<Symbol> expectedSymbols;
    private Symbol actualSymbol;

    public UnexpectedSymbolException(Set<Symbol> expectedSymbols, Symbol actualSymbol) {
        this.expectedSymbols = expectedSymbols;
        this.actualSymbol = actualSymbol;
    }

    public UnexpectedSymbolException(Symbol topSymbol, Symbol first) {
        expectedSymbols = new HashSet<Symbol>();
        expectedSymbols.add(topSymbol);
        actualSymbol = first;
    }

    public void printMessage(PrintStream writer) {
        writer.println("Unexpected token encountered:");
        writer.print("\tExpected: " + expectedSymbols.toString());
        writer.print(", Found: " + actualSymbol.toString() + "\n");
        if (actualSymbol instanceof Token) {
            writer.print("\tNear: \""+ ((Token) actualSymbol).getValue() +"\"");
        }
    }
}
