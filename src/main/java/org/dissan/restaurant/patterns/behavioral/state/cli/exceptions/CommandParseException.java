package org.dissan.restaurant.patterns.behavioral.state.cli.exceptions;

public class CommandParseException extends Exception {
    public CommandParseException() {
        super("invalid command not allowed");
    }
}
