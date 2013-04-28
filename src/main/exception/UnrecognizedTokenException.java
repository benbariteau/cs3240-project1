package main.exception;

import java.io.PrintStream;

public class UnrecognizedTokenException extends Exception {
    String s;
    int location;

    public UnrecognizedTokenException() {}

    public UnrecognizedTokenException(String s, int i) {
        this.s = s;
        location = i;
    }

    public void printMessage(PrintStream stream) {
        stream.println("Unrecognized token encountered:");
        int startContext = location - 20 >= 0 ? location - 20 : 0;
        int endContext = location + 20 < s.length() ? location + 20 : s.length();
        String context = s.substring(startContext, endContext);
        stream.println("\tNear: " + context);
    }
}
