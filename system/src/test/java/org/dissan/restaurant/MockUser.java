package org.dissan.restaurant;

import org.dissan.restaurant.models.ConcreteUser;
import org.dissan.restaurant.models.exceptions.UserRoleException;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

public class MockUser {

    private static final String TEST = "TEST";
    public static @NotNull ConcreteUser getUser() throws UserRoleException {
        ConcreteUser user = new ConcreteUser() {};
        user.setUsername(MockUser.TEST);
        user.setName(MockUser.TEST);
        user.setSurname(MockUser.TEST);
        user.setCityOfBirth(MockUser.TEST);
        try {
            user.setDateOfBirth("1999-10-10");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
