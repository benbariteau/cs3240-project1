package main.grammar;

import java.util.Set;

/**
 * An interface that represents a symbol
 */
public interface Symbol {

	public Set<Symbol> getFirstSet();

}
