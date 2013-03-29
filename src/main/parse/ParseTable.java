package main.parse;

import main.grammar.EmptyString;
import main.grammar.EndOfInput;
import main.grammar.Production;
import main.grammar.Rule;
import main.grammar.Symbol;
import main.grammar.Terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * A class that represents a parse table
 */
public class ParseTable {
	Map<Rule, Map<Symbol, Production>> table;

	/**
	 * Constructor for the parse table
	 */
	public ParseTable(Map<Rule, Map<Symbol, Production>> table) {
		this.table = table;
	}

	/**
	 * Parse the input string
	 */
	public ParseTree parse(String input, Rule startVariable) {
		ParseTree tree = new ParseTree(startVariable);

		List<Symbol> inputSymbols = stringToSymbolList(input);
		Deque<ParseNode> parseStack = new ArrayDeque<ParseNode>();
		parseStack.push(new ParseNode(new EndOfInput()));
		parseStack.push(tree.getHead());

		while (inputSymbols.size() > 0) {
			ParseNode top = parseStack.peek();
			Symbol topSymbol = top.getSymbol();
			Symbol first = inputSymbols.get(0);

			if (topSymbol instanceof Rule) {
				Production p = table.get(topSymbol).get(first);
				parseStack.pop();

				List<ParseNode> parseNodes = p.getParseNodes();

				top.addChildren(parseNodes);

				for (int i = parseNodes.size() - 1; i >= 0; i--) {
					ParseNode parseNode = parseNodes.get(i);
					if (!(parseNode.getSymbol() instanceof EmptyString)) {
						parseStack.push(parseNode);
					}
				}
			} else {
				if (first.equals(topSymbol)) {
					parseStack.pop();
					inputSymbols.remove(0);
				}
			}
		}
		return tree;
	}

	/**
	 * Convert the string input to a list of Symbols
	 */
	private static List<Symbol> stringToSymbolList(String input) {
		List<Symbol> symbolList = new ArrayList<Symbol>();
		for (char c : input.toCharArray()) {
			symbolList.add(new Terminal(c));
		}
		symbolList.add(new EndOfInput());
		return symbolList;
	}

}
