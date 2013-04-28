package main;

import main.exception.UnrecognizedTokenException;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LabelledDFA {
    DFA dfa;
    Map<State, Set<String>> acceptMap;
    State currentState;
    private String lastToken;
    Map<String, Integer> tokenPriority;

    public static final int NON_TOKEN_WHITESPACE = 0;
    public static final int TOKEN_END = 1;
    public static final int REGALAR = 2;

    public LabelledDFA() {
        dfa = null;
        acceptMap = new HashMap<State, Set<String>>();
    }

    public void setTokenPriority(List<String> tokens) {
        tokenPriority = new HashMap<String, Integer>();
        for (int i = 0; i < tokens.size(); i++) {
            tokenPriority.put(tokens.get(i), i);
        }
    }

    public String run(String s) throws UnrecognizedTokenException {
        currentState = dfa.startState;
        int i = 0;
        try {
            for (i = 0; i < s.length(); i++) {
                Set<Integer> status = next(s.charAt(i));
                if (status.contains(TOKEN_END)) {
                    if (i == 0 && s.length() == 1) {
                        return s;
                    }
                    return s.substring(0, i);
                }
            }
            if (next('\0').contains(TOKEN_END)) {
                return s.substring(0);
            }
        } catch (UnrecognizedTokenException e) {
            throw new UnrecognizedTokenException(s, i);
        }
        return null;
    }

    public Set<Integer> next(char c) throws UnrecognizedTokenException {
        if (currentState == null) {
            currentState = dfa.startState;
        }

        Set<Integer> statuses = new HashSet<Integer>();

        State nextState = dfa.table.get(currentState).get(c);
        State nextFromStart = dfa.table.get(dfa.startState).get(c);

        if (nextState == null) {
            if (currentState != dfa.startState) {
                Set<String> tokenIds = acceptMap.get(currentState);
                if (tokenIds == null) {
                    throw new UnrecognizedTokenException();
                }
                lastToken = findPriorityToken(tokenIds);
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

    private String findPriorityToken(Set<String> strings) throws UnrecognizedTokenException {
        String priorityToken = null;
        for (String s : strings) {
            if (priorityToken == null) {
                priorityToken = s;
            } else if (getTokenPriority(priorityToken) > getTokenPriority(s)) {
                priorityToken = s;
            }
        }
        return priorityToken;
    }

    private int getTokenPriority(String token) {
        Integer priority = tokenPriority.get(token);
        if (priority == null) {
            priority = tokenPriority.get("$" + token);
        }
        return priority;
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
                    Set<String> labels = ldfa.acceptMap.get(dfaStateMap.get(s));
                    if (labels == null) {
                        labels = new HashSet<String>();
                    }
                    labels.add(nfas.get(nfa));
                    ldfa.acceptMap.put(dfaStateMap.get(s), labels);
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
