package org.dissan.restaurant.patterns.creational.factory;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.patterns.behavioral.state.cli.*;
import org.dissan.restaurant.patterns.behavioral.state.cli.exceptions.CliUiException;
import org.jetbrains.annotations.NotNull;

public class StateFactory {
    private StateFactory() {}

    private static final String UI_MESSAGE = "ui does not exist";
    public static @NotNull CliState getInstance(@NotNull CliStateEnum state, CliState currentState) throws CliUiException {
        CliState newState;
        switch (state){
            case HOME:
                newState = new HomeCliState();
                break;
            case LOGIN:
                newState = new LoginCliState(currentState);
                break;
            case ASSIGN_SHIFT:
                newState = new AssignShiftCliState(currentState);
                break;
            default:
                throw new CliUiException(UI_MESSAGE);
        }

        return newState;
    }

    public static @NotNull CliState getInstance(@NotNull CliStateEnum cliStateEnum, UserBean bean, CliState currentState) throws CliUiException {
        switch (cliStateEnum){
            case ATTENDANT:
                return new AttendantHomeCliState(bean, currentState);
            case COOKER:
            case MANAGER:
                return new ManagerHomeCliState(bean, currentState);
            case HOME:
            case LOGIN:
                return getInstance(cliStateEnum, currentState);
            case ASSIGN_SHIFT:
            default:
                throw new CliUiException(UI_MESSAGE);
        }
    }
}
