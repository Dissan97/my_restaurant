package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanEmployeeApi;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class EmployeeHomeCliState extends AccountHomeCliState{

    protected static final String MANAGE_SCHEDULE = "manage_update";

    protected ShiftManagerEmployeeApi shiftManager;
    protected ShiftScheduleBeanEmployeeApi shiftBean;
    protected EmployeeBean employeeBean;

    protected EmployeeHomeCliState(String className, @NotNull UserBean userBean) {
        super(className, userBean);
        addCmd(MANAGE_SCHEDULE);
        shiftManager = new ShiftManager();
        shiftBean = shiftManager.getBean();
        LoginController controller = new LoginController();
        this.employeeBean = controller.getEmployeeBean(userBean);
    }

    protected void manageSchedule() throws EmployeeDaoException {
        UpdateEmployeeShiftCliState updateEmployeeShift = new UpdateEmployeeShiftCliState(this.pageName, userBean);
        updateEmployeeShift.previousState = this;
        updateEmployeeShift.updateUi();
    }

    private void parseRequestInput(int op) {
        String entry;
        String message;
        ShiftBeanCommand command;
        switch (op) {
            case 0:
                message = "shift code";
                command = ShiftBeanCommand.SHIFT_CODE;
                break;
            case 1:
                message = "shift date";
                command = ShiftBeanCommand.EMPLOYEE_CODE;
                break;
            default:
                return;
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
