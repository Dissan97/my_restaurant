package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.dao.user.UserDao;

public class AttendantHomeCliState extends EmployeeHomeCliState {
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
            case "order_to_deliver":
            case "1":
                order_to_deliver();
                break;
            case "handle_bill":
            case "2":
                handleBill();
                break;
            case "help":
            case "3":
                showHelp();
                break;
            case "exit":
            case "4":
                return;
            case "back":
            case "5":
                changeState(previousState);
                return;
            case "account":
            case "6":
                showAccountInfo();
                break;
            case REQUEST_UPDATE:
            case "7":
                requestUpdate();
                break;
            case VIEW_SCHEDULES:
            case "8":
                viewSchedules();
                break;
            default:
                logger.warning("Something wrong");
                break;
        }
        updateUi();
    }

    private void handleBill() {
        //to implement
    }

    private void order_to_deliver() {
        //to implement
    }

    public static void main(String[] args) {
        UserDao dao = new UserDao();
        AbstractUser user = dao.getUserByUsername("attendant");
        UserBean ub = new UserBean();
        ub.setUser(user);
        AttendantHomeCliState cliState = new AttendantHomeCliState(ub, null);
        cliState.updateUi();

    }
}
