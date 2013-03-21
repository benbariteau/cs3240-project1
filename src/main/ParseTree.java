package main;

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
