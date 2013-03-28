package main.grammar;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents an empty string symbol
 */
public class EmptyString implements Symbol {

	@Override
	public boolean equals(Object obj) {
		return obj instanceof EmptyString;
	}

	@Override
	public Set<Symbol> getFirstSet() {
		Set<Symbol> firstSet = new HashSet<Symbol>();
		firstSet.add(this);
		return firstSet;
	}

	@Override
	public String toString() {
		return "\u03B5";
	}

	@Override
	public int hashCode() {
		return 0;
	}
	
}
