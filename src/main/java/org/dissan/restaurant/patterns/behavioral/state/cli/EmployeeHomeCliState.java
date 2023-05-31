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
import org.dissan.restaurant.models.Employee;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class EmployeeHomeCliState extends AccountHomeCliState{

    protected static final String REQUEST_UPDATE  = "request_schedule_update";
    protected static final String VIEW_SCHEDULES = "view_schedules";
    protected ShiftManagerEmployeeApi shiftManager;
    protected ShiftScheduleBeanEmployeeApi shiftBean;
    protected EmployeeBean employeeBean;

    protected EmployeeHomeCliState(String className, @NotNull UserBean userBean) {
        super(className, userBean);
        addCmd(REQUEST_UPDATE);
        addCmd(VIEW_SCHEDULES);
        shiftManager = new ShiftManager();
        shiftBean = shiftManager.getBean();
        LoginController controller = new LoginController();
        this.employeeBean = controller.getEmployeeBean(userBean);
    }

    protected void requestUpdate(){
        viewSchedules();
        parseRequestInput(0);
        this.shiftBean.clean();
        try {
            this.shiftBean.insertCommand(ShiftBeanCommand.EMPLOYEE_CODE, this.employeeBean.getCode());
            this.shiftManager.requestUpdate();
        } catch (EmployeeDaoException | ShiftDaoException | ShiftScheduleDaoException | ShiftDateException |
                 BadCommanEntryException e) {
            outline("update request failed: " + e.getMessage());
        }
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

    protected void viewSchedules(){
        this.shiftManager.getMySchedule(this.employeeBean.getCode());
        Map<Integer, String> shiftSchedules = this.shiftBean.getMyShiftSchedules();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, String> entry:
             shiftSchedules.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        }
        out(builder.toString());
    }


}