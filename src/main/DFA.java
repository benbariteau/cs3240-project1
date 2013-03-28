package main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFA {
    State startState;
    Map<State, Map<Character, State>> table;
    Set<State> acceptStates;

    public DFA() {
        table = new HashMap<State, Map<Character, State>>();
        acceptStates = new HashSet<State>();
    }

    public static DFA createFromNFA(NFA nfa) {
        DFA dfa = new DFA();

        Map<Set<State>, Map<Character, Set<State>>> preTable = new HashMap<Set<State>, Map<Character, Set<State>>>();

        Map<Set<State>, Set<State>> dfaStates = new HashMap<Set<State>, Set<State>>();
        Set<State> startState = getEpsilonStateSet(nfa, nfa.startState);
        dfaStates.put(startState, startState);

        List<Set<State>> newStates = new ArrayList<Set<State>>();
        newStates.add(startState);

        while (newStates.size() > 0) {
            List<Set<State>> newNewStates = new ArrayList<Set<State>>();
            for (Set<State> s : newStates) {
                Map<Character, Set<State>> transitions = new HashMap<Character, Set<State>>();
                for (char c : Main.printableAscii) {
                    Set<State> stateSet = getEpsilonStateSet(nfa, getStateSet(nfa, s, c));
                    Set<State> getSet = dfaStates.get(stateSet);
                    if (getSet == null && stateSet != null) {
                        newNewStates.add(stateSet);
                    } else {
                        stateSet = getSet;
                    }
                    transitions.put(c, stateSet);
                }
                preTable.put(s, transitions);
            }
            for (Set<State> s: newStates) {
                dfaStates.put(s, s);
            }
            newStates = newNewStates;
        }

        Map<Set<State>, State> dfaMap = new HashMap<Set<State>, State>();
        for (Set<State> s : dfaStates.keySet()) {
            boolean isAccept = false;
            String name = "{";
            for (State state : s) {
                isAccept = isAccept || nfa.acceptStates.contains(state);
                name += state.name +",";
            }
            name += "}";
            State state = new State(name);
            dfaMap.put(s, state);
            if (isAccept) {
                dfa.acceptStates.add(state);
            }
        }

        dfa.startState = dfaMap.get(startState);
        for (Set<State> key : preTable.keySet()) {
            Map<Character, State> transitions = new HashMap<Character, State>();
            for (Character c : preTable.get(key).keySet()) {
                transitions.put(c, dfaMap.get(preTable.get(key).get(c)));
            }
            dfa.table.put(dfaMap.get(key), transitions);
        }

        return dfa;
    }

    private static Set<State> getEpsilonStateSet(NFA nfa, State state) {
        Set<State> stateSet = new HashSet<State>();
        stateSet.add(state);
        return getEpsilonStateSet(nfa, stateSet);
    }

    private static Set<State> getEpsilonStateSet(NFA nfa, Set<State> states) {
        Set<State> stateSet = new HashSet<State>();
        for (State state : states) {
            Map<Character, Set<State>> map = nfa.table.get(state);
            if(map != null)  {
                Set<State> s = map.get(null);
                if (s != null) {
                    stateSet.addAll(s);
                }
            }
        }
        if (!stateSet.isEmpty()) {
            stateSet = getEpsilonStateSet(nfa, stateSet);
        }
        stateSet.addAll(states);
        return stateSet;
    }

    private static Set<State> getStateSet(NFA nfa, Set<State> states, Character transition) {
        Set<State> stateSet = new HashSet<State>();
       for (State state : states) {
            Map<Character, Set<State>> map = nfa.table.get(state);
            if (map != null) {
                Set<State> s = map.get(transition);
                if(s != null)  {
                    stateSet.addAll(s);
                }
            }
        }
        return stateSet;
    }

    @Override
    public String toString() {
        String s = "";
        for (State state : table.keySet()) {
            boolean st = state == startState;
            boolean a = acceptStates.contains(state);
            s += (st?">":"") + (a?"(":"") +state + (a?")":"") + " [";

            Map<Character, State> transitions = table.get(state);
            for (Character c : transitions.keySet()) {
                s += c + "[" +transitions.get(c) + "] ";
            }
            s += "]\n";
        }

        s += "Accept States: {";
        for (State state : acceptStates) {
            s += state + ", ";
        }
        s+= "}";
        return s;
    }
    
    public void createCSV(String filepath)
    {
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
                writer.write((st?"Start State > ":"") + (a?"Accept State: ":"") +state.toString().replaceAll(",", "+") + " ,");

                Map<Character, State> transitions = table.get(state);
                for (Character c : cArray) 
                {
                    writer.write(transitions.get(c).toString().replaceAll(",","+") + ",");
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
    	}
    	catch(Exception e)
    	{
    		System.out.println("I/O error in DFA table output: "+e);
    	}
    	
    	
    }
}
