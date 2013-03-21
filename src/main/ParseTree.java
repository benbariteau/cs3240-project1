package main;

public class ParseTree {
    ParseNode head;

    public ParseTree(Rule rule) {
        head = new ParseNode(rule);
    }
}
