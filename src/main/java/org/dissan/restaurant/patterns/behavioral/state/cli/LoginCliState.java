package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;
import org.dissan.restaurant.patterns.behavioral.state.cli.exceptions.CliUiException;
import org.dissan.restaurant.patterns.creational.factory.StateFactory;
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
            } catch (UserAlreadyExistException | BadCommanEntryException | IOException | UserCredentialException e) {
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

    private void handleCmd(@NotNull String cmd) throws UserAlreadyExistException, BadCommanEntryException, CliUiException, IOException, UserCredentialException {
        switch (cmd) {
            case "sign_in":
            case "1":                signIn(0);
                CliState state = StateFactory.getInstance(CliStateEnum.valueOf(this.bean.getRole().name()), bean, this);
                this.changeState(state);
                break;
            case "sign_up":
            case "2":
                signUp(0);
                updateUi();
                break;
            case EXIT:
            case "3":
                break;
            case "help":
            case "4":
                showHelp();
                break;
            case BACK:
            case "5":
                getBack();
                break;
            default:
                super.logger.warning("Something wrong");
                updateUi();
                break;
        }
    }

    private void signUp(int op) throws CliUiException, UserAlreadyExistException, BadCommanEntryException, IOException {
        try {
            while (op < 7) {
                switch (op) {
                    case 0:
                        insertOp(UserBeanCommand.USERNAME, UserBeanCommand.USERNAME.name().toLowerCase());
                        op = 1;
                        break;
                    case 1:
                        insertOp(UserBeanCommand.PASSWORD, UserBeanCommand.PASSWORD.name().toLowerCase());
                        op = 2;
                        break;
                    case 2:
                        insertOp(UserBeanCommand.NAME, UserBeanCommand.NAME.name().toLowerCase());
                        op = 3;
                        break;
                    case 3:
                        insertOp(UserBeanCommand.SURNAME, UserBeanCommand.SURNAME.name().toLowerCase());
                        op = 4;
                        break;
                    case 4:
                        insertOp(UserBeanCommand.CITY_OF_BIRH, UserBeanCommand.CITY_OF_BIRH.name().toLowerCase());
                        op = 5;
                        break;
                    case 5:
                        insertOp(UserBeanCommand.DATE, UserBeanCommand.DATE.name().toLowerCase());
                        op = 6;
                        break;
                    case 6:
                        insertOp(UserBeanCommand.ROLE, UserBeanCommand.ROLE.name().toLowerCase());
                        op = 7;
                        break;
                    default:
                        break;
                }
            }

        }catch (BadCommanEntryException e) {
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

    private void signIn(int op) throws UserCredentialException, CliUiException, BadCommanEntryException {

        try {
            while (op < 2) {
                switch (op) {
                    case 0:
                        insertOp(UserBeanCommand.USERNAME, UserBeanCommand.USERNAME.name().toLowerCase());
                        op = 1;
                        break;
                    case 1:
                        op = 2;
                        insertOp(UserBeanCommand.PASSWORD, UserBeanCommand.PASSWORD.name().toLowerCase());
                        break;
                    default:
                        op = 3;
                        break;
                }
            }
        }catch (BadCommanEntryException e) {
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

    private void insertOp(UserBeanCommand command, String cmd) throws BadCommanEntryException {
        String msg = super.getUserInput(cmd);
        this.bean.insertCommand(command, msg);
    }

}
