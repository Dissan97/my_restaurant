package org.dissan.restaurant;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.cli.patterns.behavioral.creational.factory.StateFactory;
import org.dissan.restaurant.cli.patterns.behavioral.state.*;
import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.dissan.restaurant.models.AbstractUser;
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
public class TestStateFactory {


    private final UserBean userBean;
    private final CliStateEnum cliStateEnum;
    private CliState state = null;


    public TestStateFactory(CliStateEnum cliState) throws UserRoleException {

        userBean = new UserBean();
        AbstractUser user = MockUser.getUser();
        if (cliState != CliStateEnum.HOME && cliState != CliStateEnum.LOGIN) {
            user.setRole(cliState.name());
        }
        userBean.setUser(user);
        this.cliStateEnum = cliState;

    }

    @Contract(pure = true)
    @Parameterized.Parameters
    public static @NotNull Collection<CliStateEnum> getTestParameters(){
        return Arrays.asList(
                CliStateEnum.HOME, CliStateEnum.LOGIN, CliStateEnum.ATTENDANT, CliStateEnum.COOKER,CliStateEnum.MANAGER);
    }

    @Test
    public void testStates(){
        boolean isNull = false;
        try {
            this.state = StateFactory.getInstance(this.cliStateEnum, userBean, null);
        } catch (CliUiException e) {
            //do no op
            isNull = true;
        }

        boolean assertVal = switch (this.cliStateEnum){
            case HOME -> state instanceof HomeCliState;
            case LOGIN -> state instanceof LoginCliState;
            case ATTENDANT -> state instanceof AttendantHomeCliState;
            case MANAGER -> state instanceof ManagerHomeCliState;
            case COOKER -> state instanceof CookerHomeCliState;
        };

        if (isNull){
            assertVal = true;
        }

        assertTrue(assertVal);
    }
}
