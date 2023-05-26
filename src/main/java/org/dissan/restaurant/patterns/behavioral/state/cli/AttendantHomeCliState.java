package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;

public class AttendantHomeCliState extends AccountHomeCliState {
    public AttendantHomeCliState(UserBean bean, CliState currentState) {
        super(AttendantHomeCliState.class.getSimpleName(), bean);
        previousState = currentState;
    }

    @Override
    public void updateUi() {
        String cmd = super.getUserInput();

        if (super.parseCmd(cmd)) {
            this.updateUi();
            return;
        }

        switch (cmd) {
            case "account":
                showAccountInfo();
                break;
            case "sign_in":
                updateUi();
                break;
            case "help":
                showHelp();
                break;
            case "exit":
                break;
            default:
                logger.warning("Something wrong");
                updateUi();
                break;
        }

    }
}
