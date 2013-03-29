package main;

/**
 * A class that represents a state
 */
public class State {
    String name;

    static int stateNum = 0;

    /**
     * Constructor for the state
     */
    public State() {
        this.name = "" + stateNum++;
    }

    public State(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (name != null ? !name.equals(state.name) : state.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
}
