package main;

public class State {
    String name;

    static int stateNum = 0;

    public State(String name) {
        this.name = name + stateNum++;
    }

    @Override
    public String toString() {
        return name;
    }
}
