package org.dissan.restaurant.cli.patterns.behavioral.creational.factory;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.cli.patterns.behavioral.state.*;
import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.jetbrains.annotations.NotNull;

public class StateFactory {
    private StateFactory() {}

    private static final String UI_MESSAGE = "ui does not exist";
    public static @NotNull CliState getInstance(@NotNull CliStateEnum state, CliState currentState) throws CliUiException {
        return switch (state) {
            case HOME -> new HomeCliState();
            case LOGIN -> new LoginCliState(currentState);
            default -> throw new CliUiException(UI_MESSAGE);
        };
    }

    public static @NotNull CliState getInstance(@NotNull CliStateEnum cliStateEnum, UserBean bean, CliState currentState) throws CliUiException {
        return switch (cliStateEnum) {
            case ATTENDANT -> new AttendantHomeCliState(bean, currentState);
            case COOKER, MANAGER -> new ManagerHomeCliState(bean, currentState);
            case HOME, LOGIN -> getInstance(cliStateEnum, currentState);
        };
    }
}