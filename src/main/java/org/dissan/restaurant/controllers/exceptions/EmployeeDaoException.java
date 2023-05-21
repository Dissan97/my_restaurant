package org.dissan.restaurant.controllers.exceptions;

public class EmployeeDaoException extends Exception{

    public EmployeeDaoException(String msg) {
        super(msg + " does not match");
    }
}
