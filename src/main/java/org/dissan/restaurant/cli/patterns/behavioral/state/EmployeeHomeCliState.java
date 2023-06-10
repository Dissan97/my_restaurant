package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanEmployeeApi;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.jetbrains.annotations.NotNull;

public abstract class EmployeeHomeCliState extends AccountHomeCliState{

    protected static final String MANAGE_SCHEDULE = "manage_update";

    protected ShiftManagerEmployeeApi shiftManager;
    protected ShiftScheduleBeanEmployeeApi shiftBean;
    protected EmployeeBean employeeBean;

    protected static final String REQUEST_UPDATE = "request_update";
    protected EmployeeHomeCliState(String className, @NotNull UserBean userBean) {
        super(className, userBean);
        addCmd(MANAGE_SCHEDULE);
        shiftManager = new ShiftManager();
        shiftBean = shiftManager.getBean();
        LoginController controller = new LoginController();
        this.employeeBean = controller.getEmployeeBean(userBean);
        this.addCmd(REQUEST_UPDATE);
    }

    protected void manageSchedule() throws EmployeeDaoException {
        UpdateEmployeeShiftCliState updateEmployeeShift = new UpdateEmployeeShiftCliState(userBean);
        updateEmployeeShift.previousState = this;
        updateEmployeeShift.updateUi();
    }

    protected void parseRequestInput(int op) {
        String entry;
        String message;
        ShiftBeanCommand command;
        switch (op) {
            case 0 -> {
                message = "shift code";
                command = ShiftBeanCommand.SHIFT_CODE;
            }
            case 1 -> {
                message = "shift date";
                command = ShiftBeanCommand.EMPLOYEE_CODE;
            }
            default -> {
                return;
            }
        }
        entry = getUserInput(message);

        try {
            this.shiftBean.insertCommand(command, entry);
        } catch (BadCommanEntryException e) {
            outline(e.getMessage());
            entry = getUserInput("continue ? y - n");
            if (entry.equalsIgnoreCase("y") || entry.equalsIgnoreCase("yes")){
                parseRequestInput(op);
            }
        }

        if (op < 1){
            parseRequestInput(op + 1);
        }
    }



}
