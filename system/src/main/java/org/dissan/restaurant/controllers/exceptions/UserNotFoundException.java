package org.dissan.restaurant.controllers.exceptions;

public class UserNotFoundException extends UserCredentialException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
