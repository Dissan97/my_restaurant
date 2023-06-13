package org.dissan.restaurant.patterns.creational.factory;

import org.dissan.restaurant.models.*;
import org.dissan.restaurant.models.exceptions.UserRoleException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoleFactory {
    private RoleFactory(){}

    @Contract("_ -> new")
    public static @NotNull AbstractModelRole getInstance(AbstractUser user) throws UserRoleException {
        try {
            return switch (user.getRole()) {
                case ATTENDANT -> new Attendant(user);
                case COOKER -> new Cooker(user);
                case MANAGER -> new Manager(user);
            };
        }catch (NullPointerException e){
            throw new UserRoleException();
        }
    }
}
