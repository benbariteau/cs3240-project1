package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LabelledDFA {
    DFA dfa;
    Map<State, String> acceptMap;
    State currentState;
    private String lastToken;

    public static final int NON_TOKEN_WHITESPACE = 0;
    public static final int TOKEN_END = 1;
    public static final int REGALAR = 2;

    public LabelledDFA() {
        dfa = null;
        acceptMap = new HashMap<State, String>();
    }

    public Set<Integer> next(char c) {
        if (currentState == null) {
            currentState = dfa.startState;
        }

        Set<Integer> statuses = new HashSet<Integer>();

        State nextState = dfa.table.get(currentState).get(c);
        State nextFromStart = dfa.table.get(dfa.startState).get(c);

        if (nextState == null) {
            if (currentState != dfa.startState) {
                lastToken = acceptMap.get(currentState);
                statuses.add(TOKEN_END);
            }

            if (nextFromStart == null) {
                currentState = dfa.startState;
                statuses.add(NON_TOKEN_WHITESPACE);
            } else {
                currentState = nextFromStart;
                statuses.add(REGALAR);
            }
        } else {
            currentState = nextState;
            statuses.add(REGALAR);
        }
        return statuses;
    }

    public static LabelledDFA createFromNFAs(Map<NFA, String> nfas) {
        LabelledDFA ldfa = new LabelledDFA();

        ldfa.dfa = DFA.createFromNFAs(nfas.keySet());

        Map<Set<State>, State> dfaStateMap = ldfa.dfa.getDfaMap();
        for (NFA nfa : nfas.keySet()) {
            Set<State> acceptStates = nfa.acceptStates;
            for (Set<State> s :  dfaStateMap.keySet()) {
                boolean isAccept = false;
                for (State st : s) {
                    isAccept = isAccept || acceptStates.contains(st);
                }

                if (isAccept) {
                    ldfa.acceptMap.put(dfaStateMap.get(s), nfas.get(nfa));
                }
            }
        }

        return ldfa;
    }

    public String toString() {
        String s = dfa.toString();

        s += "\nAcceptTokens: {";
        for (State st : acceptMap.keySet()) {
            s += st + ": " + acceptMap.get(st)+",";
        }
        s += "}";

        return s;
    }

    public String getLastToken() {
        return lastToken;
    }
}
