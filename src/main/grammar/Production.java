package main.grammar;

import main.parse.ParseNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that represents a production (or list of symbols)
 */
public class Production {
	
	List<Symbol> symbols;

	public Production(List<Symbol> symbols) {
		this.symbols = symbols;
	}

	public Production(Symbol... symbols) {
		this.symbols = Arrays.asList(symbols);
	}

	public Production(String string) {
		List<Symbol> terminals = new ArrayList<Symbol>();
		for (char c : string.toCharArray()) {
			terminals.add(new Terminal(c));
		}
		this.symbols = terminals;
	}

	public Set<Symbol> getFirstSet(Rule rule) {
		Set<Symbol> firstSet = new HashSet<Symbol>();
		int i;
		for (i = 0; i < symbols.size(); i++) {
			if (symbols.get(i) == rule) {
				return firstSet;
			}
			Set<Symbol> elementFirst = symbols.get(i).getFirstSet();
			boolean containedEmptyString = elementFirst.remove(new EmptyString());
			firstSet.addAll(elementFirst);
			if (!containedEmptyString)
				break;
		}

		if (i >= symbols.size()) {
			firstSet.add(new EmptyString());
		}
		return firstSet;
	}

	public List<Symbol> getSymbolList() {
		return symbols;
	}

	@Override
	public String toString() {
		String s = "";
		for (Symbol symbol : symbols) {
			s += symbol.toString();
		}
		return s;
	}

	public List<ParseNode> getParseNodes() {
		List<ParseNode> nodes = new ArrayList<ParseNode>();
		for (Symbol s : symbols) {
			nodes.add(new ParseNode(s));
		}
		return nodes;
	}
	
}
