package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanApi;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.models.ModelUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ManagerHomeCliState extends AccountHomeCliState{

    private ShiftManager shiftManager;
    private final ShiftScheduleBeanApi scheduleBean;
    private Map<Integer, String> shiftCache = null;
    public ManagerHomeCliState(@NotNull UserBean bean) {
        super(ManagerHomeCliState.class.getSimpleName(),bean);
        setShiftManager(new ShiftManager());
        scheduleBean = getShiftManager().getBean();
    }

    @Override
    public void updateUi() {
        String cmd = super.getUserInput();

        if (super.parseCmd(cmd)) {
            this.updateUi();
            return;
        }

        switch (cmd) {
            case "1":
            case "assign_shift":
                assignShift();
                break;
            case "get_employee_schedule":
            case "2":
                getEmployeesSchedule();
                break;
            case "view_schedules":
            case "3":
                viewSchedules();
                break;
            case "help":
            case "4":
                showHelp();
                updateUi();
                break;
            case "exit":
            case "5":
                break;
            case "account":
            case "6":
                showAccountInfo();
                break;
            default:
                logger.warning("Something wrong");
                updateUi();
                break;
        }
    }


    private void getEmployeesSchedule() {
        this.shiftManager.pullSchedules();
        OutStream.println(this.scheduleBean.getAllShiftSchedules());
    }

    private void assignShift() {
        if (this.shiftCache == null){
            fillShiftCache();
        }

        String sCode = getUserInput("Shift code");
        String eCode = getUserInput("Employee code");
        String date = getUserInput("date of shift");

        try {
            this.shiftManager.assignShift(sCode, eCode, date);
        } catch (EmployeeDaoException | ShiftDaoException | ShiftDateException | ShiftScheduleDaoException e) {
            outline(e.getMessage());
        }finally {
            updateUi();
        }

    }

    private void fillShiftCache() {
        List<String> shifts = this.scheduleBean.getShifts();
        int index = 1;
        for (String s:
             shifts) {
            this.shiftCache.put(index++, s);
        }
    }

    private void outValidShift(){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, String> entry : this.shiftCache.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        }
        outline(builder.toString());
    }

    private void viewSchedules() {
        this.shiftManager.pullSchedules();
        String schedules = this.scheduleBean.getAllShiftSchedules();
        outline(schedules);
        updateUi();
    }

    public ShiftManager getShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(ShiftManager shiftManager) {
        this.shiftManager = shiftManager;
    }
}
