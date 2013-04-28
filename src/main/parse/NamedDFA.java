package main.parse;

import main.DFA;
import main.NFA;

public class NamedDFA {
    String name;
    DFA dfa;

    public NamedDFA(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Output run(String input) {
        String token = "";
        dfa.start();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            boolean done = dfa.next(c);
            if (done) {
                if (dfa.isAccept()){
                    Output output = new Output();
                    output.token =  token;
                    output.offset = i;
                    return output;
                } else {
                    return null;
                }
            }
            token += c;
        }
        if (dfa.next('\0') && dfa.isAccept()) {
            Output output = new Output();
            output.token =  token;
            output.offset = input.length();
            return output;

        }
        return null;
    }

    public static NamedDFA createFromNFA(String name, NFA nfa) {
        NamedDFA ndfa = new NamedDFA(name);
        ndfa.dfa = DFA.createFromNFA(nfa);
        return ndfa;
    }

    public class Output {
        public String token;
        public int offset;
    }

    public String toString() {
        return name;
    }
}
