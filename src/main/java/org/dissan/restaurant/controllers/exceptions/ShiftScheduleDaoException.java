package org.dissan.restaurant.controllers.exceptions;

public class ShiftScheduleDaoException extends Exception {
    public ShiftScheduleDaoException() {
        super("Shift schedule not found");
    }
}
