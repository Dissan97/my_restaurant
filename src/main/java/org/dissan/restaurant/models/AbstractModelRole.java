package org.dissan.restaurant.models;

public abstract class AbstractModelRole {
    protected AbstractUser user;

    protected AbstractModelRole(AbstractUser user) {
        this.user = user;
    }
}
