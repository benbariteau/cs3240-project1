package main;

import java.util.Arrays;
import java.util.List;

public class ParseNode {
    Rule rule;
    String string;
    List<ParseNode> children;

    public ParseNode(Rule rule) {
        this.rule = rule;
    }

    public ParseNode(String string) {
        this.string = string;
    }

    public void addChildren(List<ParseNode> children) {
        this.children = children;
    }

    public void addChildren(ParseNode... nodes) {
        this.children = Arrays.asList(nodes);
    }
}
