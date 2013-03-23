package main;

import java.util.Set;

/**
 * A class that represents an end of input symbol
 */
public class EndOfInput implements Symbol {
	
	@Override
	public Set<Symbol> getFirstSet() {
		return null;
	}

	@Override
	public String toString() {
		return "<$>";
	}

	public int hashCode() {
		return 0;
	}

	public boolean equals(Object obj) {
		return obj instanceof EndOfInput;
	}
	
}
