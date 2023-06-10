package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
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

        if (super.badParseCmd(cmd)) {
            this.updateUi();
            return;
        }

        switch (cmd) {
            case "order_to_deliver", "1" -> orderToDeliver();
            case "handle_bill", "2" -> handleBill();
            case "help", "3" -> showHelp();
            case "exit", "4" -> {
                return;
            }
            case "back", "5" -> {
                changeState(previousState);
                return;
            }
            case "account", "6" -> showAccountInfo();
            case MANAGE_SCHEDULE, "7" -> {
                try {
                    manageSchedule();
                } catch (EmployeeDaoException e) {
                    outline(e.getMessage());
                }
            }
            default -> logger.warning("Something wrong");
        }
        updateUi();
    }

    private void handleBill() {
        //to implement
    }

    private void orderToDeliver() {
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
