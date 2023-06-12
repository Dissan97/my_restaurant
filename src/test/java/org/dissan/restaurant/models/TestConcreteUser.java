package org.dissan.restaurant.models;

import org.dissan.restaurant.models.exceptions.UserRoleException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class TestConcreteUser {

    private final UserRole roles;

    @Contract(pure = true)
    @Parameterized.Parameters
    public static @NotNull Collection<UserRole> getTestParameters(){
        return Arrays.asList(
                UserRole.ATTENDANT, UserRole.COOKER, UserRole.MANAGER, null);
    }


    public TestConcreteUser(UserRole exp){
        this.roles = exp;
    }

    @Test
    public void testConcreteUser(){



        boolean assertVal;
        ConcreteUser user;
        try {
            user = MockUser.getUser();
            user.setRole(this.roles);
            user.switchRole();
            assertVal = switch (this.roles) {
                case ATTENDANT -> user.getConcreteRole() instanceof Attendant;
                case COOKER -> user.getConcreteRole() instanceof Cooker;
                case MANAGER -> user.getConcreteRole() instanceof Manager;
            };
        } catch (UserRoleException e) {
            assertVal = true;
        }

        assertTrue(assertVal);
    }
}
