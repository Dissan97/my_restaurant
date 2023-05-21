package org.dissan.restaurant.controllers.exceptions;

import java.text.ParseException;

public class ShiftDateException extends Exception{

    public ShiftDateException(String msg) {
        super(msg);
    }
}
