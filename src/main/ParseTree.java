package main;

import main.grammar.Rule;

/**
 * A class that represents an entire parse tree
 */
public class ParseTree {
	
	ParseNode head;

	public ParseTree(Rule rule) {
		head = new ParseNode(rule);
	}

	public ParseNode getHead() {
		return head;
	}

	@Override
	public String toString() {
		return head.toString();
	}

	public String getInputString() {
		return head.getInputString();
	}

}
