package org.dissan.restaurant.controllers.exceptions;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}