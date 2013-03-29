package main;

import java.io.FileWriter;
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
    
    /**
     * Write the DFA as a CSV file
     */
    public void createCSV(String filepath)
    {
    	Map<State, Map<Character, State>> table = dfa.table;
    	State startState = dfa.startState;
    	Set<State> acceptStates = dfa.acceptStates;
        State nullState = new State("{}");
        try
        {
            FileWriter writer = new FileWriter(filepath);
            Character[] cArray = new Character[]{' ','!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', '-', '.', '/',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C',
                    'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                    'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'};
            writer.write("State,");
            for (Character c : cArray)
            {
                if(c == '\"')
                    writer.write("\"\",");
                else if (c == ',')
                    writer.write("\",\",");
                else
                    writer.write(c + ",");
            }
            writer.write("\n");
            for (State state : table.keySet())
            {

                boolean st = state == startState;
                boolean a = acceptStates.contains(state);
                writer.write((st?"Start State > ":"") + (a?"Accept State ("+ acceptMap.get(state) +"): ":"") + (state == null ? nullState.toString() :state.toString().replaceAll(",", "+")) + " ,");

                Map<Character, State> transitions = table.get(state);
                for (Character c : cArray)
                {
                    State s = transitions.get(c);
                    s = s == null ? nullState : s;
                    writer.write(s.toString().replaceAll(",","+") + ",");
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("I/O error in DFA table output: ");
            e.printStackTrace();
        }
    }

    public String getLastToken() {
        return lastToken;
    }
}
