package main.parse;

import main.grammar.EmptyString;
import main.grammar.Rule;
import main.grammar.Symbol;

import java.util.Arrays;
import java.util.List;

/**
 * A class that represents a singular parse node
 */
public class ParseNode {
    private Symbol symbol;
    List<ParseNode> children;
    String inputString;

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
        if(children != null) {
            s += "->{ ";
            for(ParseNode child : children) {
                s += child.toString() + " ";
            }
            s += "}";
        }
        return s;
    }

    public String getInputString() {
        if(inputString == null) {
            if(symbol instanceof Rule) {
                String s = "";
                if (children != null) {
                    for (ParseNode child : children) {
                        s += child.getInputString();
                    }
                }
                inputString =  s;
            } else if (symbol instanceof EmptyString) {
                inputString = "";
            } else {
                inputString = symbol.toString();
            }
        }
        return inputString;
    }

    public boolean hasEmptyChildren() {
        return children == null || children.size() == 0 || children.get(0).symbol.equals(new EmptyString());
    }
}
