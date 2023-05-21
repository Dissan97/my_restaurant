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
            switch (user.getRole()) {
                case ATTENDANT:
                    return  new Attendant(user);
                case COOKER:
                     return new Cooker(user);
                case MANAGER:
                    return new Manager(user);
                default:
                    throw new UserRoleException();
            }
        }catch (NullPointerException e){
            throw new UserRoleException();
        }
    }
}
