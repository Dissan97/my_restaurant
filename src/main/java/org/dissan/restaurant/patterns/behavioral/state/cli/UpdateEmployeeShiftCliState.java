package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanEmployeeApi;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;

public class UpdateEmployeeShiftCliState extends CliState {
    private ShiftManagerEmployeeApi shiftManager;
    private ShiftScheduleBeanEmployeeApi shiftBean;

    public UpdateEmployeeShiftCliState(String name, UserBean userBean) throws EmployeeDaoException {
        super(name);
        this.shiftManager = new ShiftManager(userBean);
        this.shiftBean = this.shiftManager.getBean();


    }

    @Override
    public void updateUi() {
        String cmd = getUserInput("insert a command");
        if (badParseCmd(cmd)){
            updateUi();
            return;
        }

        switch (cmd) {
            case "view_schedules":
            case "1":
                viewSchedules();
                break;
            case "request_update":
            case "2":
                requestUpdate();
                break;
        }
    }


    private void viewSchedules() {
        this.shiftManager.getMySchedule();
    }

    private void requestUpdate() {
    }
}
