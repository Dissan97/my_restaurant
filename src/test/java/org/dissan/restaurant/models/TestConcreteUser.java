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



        boolean assertVal = false;
        ConcreteUser user = null;
        try {
            user = MockUser.getUser();
            user.setRole(this.roles);
            user.switchRole();
            switch (this.roles) {
                case ATTENDANT:
                    assertVal = user.getConcreteRole() instanceof Attendant;
                    break;
                case COOKER:
                    assertVal = user.getConcreteRole() instanceof Cooker;
                    break;
                case MANAGER:
                    assertVal = user.getConcreteRole() instanceof Manager;
                    break;
            }
        } catch (UserRoleException e) {
            assertVal = true;
        }

        assertTrue(assertVal);
    }
}
