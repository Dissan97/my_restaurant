package org.dissan.restaurant.models.exceptions;

public class UserRoleException extends Exception {
    public UserRoleException() {
        super("role does not exist");
    }
}
