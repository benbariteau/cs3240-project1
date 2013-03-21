package main;

import java.util.Arrays;
import java.util.List;

/**
 * A class that represents a singular parse node
 */
public class ParseNode {
	
	Symbol symbol;
	List<ParseNode> children;

	public ParseNode(Symbol s) {
		symbol = s;
	}

	public void addChildren(List<ParseNode> children) {
		this.children = children;
	}

	public void addChildren(ParseNode... nodes) {
		this.children = Arrays.asList(nodes);
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public List<ParseNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		String s = symbol.toString();
		if (children != null) {
			s += "->{ ";
			for (ParseNode child : children) {
				s += child.toString() + " ";
			}
			s += "}";
		}
		return s;
	}

	public String getInputString() {
		if (symbol instanceof Rule) {
			String s = "";
			if (children != null) {
				for (ParseNode child : children) {
					s += child.getInputString();
				}
			}
			return s;
		} else if (symbol instanceof EmptyString) {
			return "";
		}
		return symbol.toString();
	}

}
