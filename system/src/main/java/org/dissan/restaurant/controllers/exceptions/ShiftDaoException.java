package org.dissan.restaurant.controllers.exceptions;

public class ShiftDaoException extends Exception {

    public ShiftDaoException(String s) {
        super(s + " does not exist");
    }
}
