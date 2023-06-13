package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;
import org.dissan.restaurant.cli.patterns.behavioral.creational.factory.StateFactory;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class LoginCliState extends CliState{

    private final LoginController controller;
    private final UserBean bean;
    private static final String CONTINUE = "continue? y to continue";
    public LoginCliState(CliState prevState) {
        super(LoginCliState.class.getSimpleName());
        super.pageName = "LOGIN";
        controller = new LoginController();
        this.bean = controller.getUserBean();
        this.previousState = prevState;
    }

    @Override
    public void updateUi() {
            String cmd = super.getUserInput();

            if (super.badParseCmd(cmd)) {
                this.updateUi();
                return;
            }
            try {
                handleCmd(cmd);
            } catch (UserAlreadyExistException | BadCommandEntryException | IOException | UserCredentialException e) {
                outline(e.getMessage());
                updateUi();
            } catch (CliUiException e) {
                CliState state;
                try {
                    state = StateFactory.getInstance(CliStateEnum.HOME, this);
                } catch (CliUiException ex) {
                    outline("Something wrong");
                    return;
                }
                state.updateUi();
            }

    }

    private void handleCmd(@NotNull String cmd) throws UserAlreadyExistException, BadCommandEntryException, CliUiException, IOException, UserCredentialException {
        switch (cmd) {
            case "sign_in", "1" -> {
                signIn(0);
                CliState state = StateFactory.getInstance(CliStateEnum.valueOf(this.bean.getRole().name()), bean, this);
                this.changeState(state);
            }
            case "sign_up", "2" -> {
                signUp(0);
                updateUi();
            }
            case HELP, "3" -> showHelp();
            case SWITCH_DAO, "4" -> {
                this.controller.switchDao();
                outline("DAO SWITCHED");
                updateUi();
            }
            case BACK, "6" -> getBack();
            default -> {
                super.logger.warning("Something wrong");
                updateUi();
            }
        }
    }

    private void signUp(int op) throws CliUiException, UserAlreadyExistException, BadCommandEntryException, IOException {
        try {
            while (op < 7) {
                switch (op) {
                    case 0 -> {
                        insertOp(UserBeanCommand.USERNAME, UserBeanCommand.USERNAME.name().toLowerCase());
                        op = 1;
                    }
                    case 1 -> {
                        insertOp(UserBeanCommand.PASSWORD, UserBeanCommand.PASSWORD.name().toLowerCase());
                        op = 2;
                    }
                    case 2 -> {
                        insertOp(UserBeanCommand.NAME, UserBeanCommand.NAME.name().toLowerCase());
                        op = 3;
                    }
                    case 3 -> {
                        insertOp(UserBeanCommand.SURNAME, UserBeanCommand.SURNAME.name().toLowerCase());
                        op = 4;
                    }
                    case 4 -> {
                        insertOp(UserBeanCommand.CITY_OF_BIRH, UserBeanCommand.CITY_OF_BIRH.name().toLowerCase());
                        op = 5;
                    }
                    case 5 -> {
                        insertOp(UserBeanCommand.DATE, UserBeanCommand.DATE.name().toLowerCase());
                        op = 6;
                    }
                    case 6 -> {
                        insertOp(UserBeanCommand.ROLE, UserBeanCommand.ROLE.name().toLowerCase());
                        op = 7;
                    }
                    default -> {
                        //no operation
                    }
                }
            }

        }catch (BadCommandEntryException e) {
            super.outline(e.getMessage());
            String cmd = super.getUserInput(LoginCliState.CONTINUE);
            if (cmd.equalsIgnoreCase("y") || cmd.equalsIgnoreCase("yes")) {
                this.signUp(op);
            }else {
                this.bean.clean();
                throw new CliUiException("No command restart...");
            }
        }

        this.controller.singUp();
    }

    private void signIn(int op) throws UserCredentialException, CliUiException, BadCommandEntryException {

        try {
            while (op < 2) {
                switch (op) {
                    case 0 -> {
                        insertOp(UserBeanCommand.USERNAME, UserBeanCommand.USERNAME.name().toLowerCase());
                        op = 1;
                    }
                    case 1 -> {
                        op = 2;
                        insertOp(UserBeanCommand.PASSWORD, UserBeanCommand.PASSWORD.name().toLowerCase());
                    }
                    default -> op = 3;
                }
            }
        }catch (BadCommandEntryException e) {
            super.outline(e.getMessage());
            String cmd = super.getUserInput(LoginCliState.CONTINUE);
            if (cmd.equalsIgnoreCase("y") || cmd.equalsIgnoreCase("yes")) {
                this.signIn(op);
            }else {
                this.bean.clean();
                throw new CliUiException("No command restart...");
            }
        }
        this.controller.singIn();
    }

    private void insertOp(UserBeanCommand command, String cmd) throws BadCommandEntryException {
        String msg = super.getUserInput(cmd);
        this.bean.insertCommand(command, msg);
    }

}
