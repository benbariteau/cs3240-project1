package main;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that represents an NFA
 */
public class NFA {
	State startState;
	Map<State, Map<Character, Set<State>>> table;
	Set<State> acceptStates;

	/**
	 * Constructor for the NFA class
	 */
	public NFA() {
		table = new HashMap<State, Map<Character, Set<State>>>();
		acceptStates = new HashSet<State>();
	}

	/**
	 * Constructs an NFA based on a character
	 */
	public static NFA constructNFAFromCharacter(char c) {
		Set<Character> set = new HashSet<Character>();
		set.add(c);
		return constructNFAFromCharacterSet(set);
	}

	/**
	 * Constructs an NFA based on a set of characters
	 */
	public static NFA constructNFAFromCharacterSet(Set<Character> characters) {
		NFA nfa = new NFA();
		nfa.startState = new State();

		State endState = new State();
		nfa.acceptStates.add(endState);

		Map<Character, Set<State>> map = new HashMap<Character, Set<State>>();
		for (Character c : characters) {
			Set<State> states = new HashSet<State>();
			states.add(endState);
			map.put(c, states);
		}
		nfa.table.put(nfa.startState, map);
		return nfa;
	}

	/**
	 * Excludes characters
	 */
	public NFA excludeCharacters(Set<Character> charSet) {
		Map<Character, Set<State>> fromStart = table.get(startState);
		if (fromStart == null) {
			fromStart = new HashMap<Character, Set<State>>();
			table.put(startState, fromStart);
		} else {
			Map<Character, Set<State>> newStartTransitions = new HashMap<Character, Set<State>>();
			for (Character c : fromStart.keySet()) {
				if (!charSet.contains(c)) {
					newStartTransitions.put(c, fromStart.get(c));
				}
			}
			table.put(startState, newStartTransitions);
		}
		return this;
	}

	/**
	 * Create a star NFA
	 */
	public NFA createStarNFA() {
		for (State state : acceptStates) {
			Map<Character, Set<State>> transitions = table.get(state);
			if (transitions == null) {
				transitions = new HashMap<Character, Set<State>>();
			}

			Set<State> states = transitions.get(null);
			if (states == null) {
				states = new HashSet<State>();
			}
			states.add(startState);
			transitions.put(null, states);
			table.put(state, transitions);
		}

		acceptStates.add(startState);
		return this;
	}

	/**
	 * Create a plus NFA
	 */
	public NFA createPlusNFA() {
		for (State state : acceptStates) {
			Map<Character, Set<State>> transitions = table.get(state);
			if (transitions == null) {
				transitions = new HashMap<Character, Set<State>>();
			}

			Set<State> states = transitions.get(null);
			if (states == null) {
				states = new HashSet<State>();
			}
			states.add(startState);
			transitions.put(null, states);
			table.put(state, transitions);
		}
		return this;
	}

	/**
	 * Create a concatenation NFA
	 */
	public NFA concatNFA(NFA concatNFA) {
		table.putAll(concatNFA.table);

		for (State state : acceptStates) {
			Map<Character, Set<State>> transitions = table.get(state);
			if (transitions == null) {
				transitions = new HashMap<Character, Set<State>>();
			}

			Set<State> states = transitions.get(null);
			if (states == null) {
				states = new HashSet<State>();
			}
			states.add(concatNFA.startState);

			transitions.put(null, states);
			table.put(state, transitions);
		}

		acceptStates = concatNFA.acceptStates;

		return this;
	}

	/**
	 * Create a union NFA
	 */
	public NFA unionNFA(NFA unionRHS) {
		table.putAll(unionRHS.table);

		State newStart = new State();

		Map<Character, Set<State>> transitions = new HashMap<Character, Set<State>>();
		Set<State> states = new HashSet<State>();
		states.add(startState);
		states.add(unionRHS.startState);
		transitions.put(null, states);
		table.put(newStart, transitions);
		startState = newStart;
		acceptStates.addAll(unionRHS.acceptStates);
		return this;
	}

	/**
	 * Combines a collection of NFA's via union operation
	 */
	public static NFA unionNFAs(Collection<NFA> nfas) {
		// Create new NFA to be the giant unioned NFA
		NFA tempNFA = new NFA();

		// Add all transitions to the NFA
		for (NFA nfa : nfas) {
			tempNFA.table.putAll(nfa.table);
		}

		// Point new NFA's start state to all the unioned NFAs
		State newStart = new State();
		Map<Character, Set<State>> transitions = new HashMap<Character, Set<State>>();
		Set<State> states = new HashSet<State>();

		for (NFA nfa : nfas) {
			states.add(nfa.startState);
		}

		transitions.put(null, states);
		tempNFA.table.put(newStart, transitions);
		tempNFA.startState = newStart;

		for (NFA nfa : nfas) {
			tempNFA.acceptStates.addAll(nfa.acceptStates);
		}

		return tempNFA;
	}

	/**
	 * Converts this NFA into a string
	 */
	@Override
	public String toString() {
		String s = "";
		for (State state : table.keySet()) {
			Map<Character, Set<State>> transitions = table.get(state);

			boolean a = acceptStates.contains(state);
			boolean st = state == startState;
			s += (st ? ">" : "") + (a ? "(" : "") + state + (a ? ")" : "") + " [";
			for (Character c : transitions.keySet()) {
				s += c + "[";
				for (State transitionState : transitions.get(c)) {
					s += transitionState + ", ";
				}
				s += "] ";
			}
			s += "]\n";
		}
		s += "Accept States: {";
		for (State state : acceptStates) {
			s += state + ", ";
		}
		s += "}\n";
		return s;
	}

	/**
	 * Completely copies this NFA into a new NFA
	 */
	public NFA clone() {
		NFA clone = new NFA();

		Map<State, State> oldToNew = new HashMap<State, State>();

		oldToNew.put(startState, new State());
		clone.startState = oldToNew.get(startState);
		clone.table = new HashMap<State, Map<Character, Set<State>>>();
		for (State state : table.keySet()) {
			State newState = oldToNew.get(state);
			if (newState == null) {
				newState = new State();
				oldToNew.put(state, newState);
			}
			Map<Character, Set<State>> transitions = new HashMap<Character, Set<State>>();
			for (Character c : table.get(state).keySet()) {
				Set<State> states = table.get(state).get(c);
				Set<State> newStates = new HashSet<State>();
				for (State s : states) {
					State ns = oldToNew.get(s);
					if (ns == null) {
						ns = new State();
						oldToNew.put(s, ns);
					}
					newStates.add(ns);
				}
				transitions.put(c, newStates);
			}
			clone.table.put(newState, transitions);
		}

		for (State accept : acceptStates) {
			State newAccept = oldToNew.get(accept);
			if (newAccept == null) {
				newAccept = new State();
				oldToNew.put(accept, newAccept);
			}
			clone.acceptStates.add(oldToNew.get(accept));
		}

		return clone;
	}
}
