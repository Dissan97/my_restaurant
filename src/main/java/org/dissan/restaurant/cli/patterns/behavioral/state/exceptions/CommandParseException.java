package org.dissan.restaurant.cli.patterns.behavioral.state.exceptions;

public class CommandParseException extends Exception {
    public CommandParseException() {
        super("invalid command not allowed");
    }
}
