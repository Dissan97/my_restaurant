package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanApi;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerHomeCliState extends AccountHomeCliState{

    private ShiftManager shiftManager;
    private final ShiftScheduleBeanApi scheduleBean;
    private Map<Integer, String> shiftCache = null;
    public ManagerHomeCliState(@NotNull UserBean bean, CliState currentState) {
        super(ManagerHomeCliState.class.getSimpleName(),bean);
        previousState = currentState;
        setShiftManager(new ShiftManager());
        scheduleBean = getShiftManager().getBean();
    }

    @Override
    public void updateUi() {
        String cmd = super.getUserInput();

        if (super.badParseCmd(cmd)) {
            this.updateUi();
            return;
        }

        switch (cmd) {
            case "1", "assign_shift" -> assignShift();
            case "get_employee_schedule", "2" -> getEmployeesSchedule();
            case "view_schedules", "3" -> viewSchedules();
            case "view_requests:", "4" -> {
                viewRequests();
                updateUi();
            }
            case "accept_request", "5" -> acceptRequest();
            case "help", "6" -> showHelp();
            case "exit", "7" -> exitProgram();
            case "account", "8" -> showAccountInfo();
            case "switch_persistence", "9" -> switchPersistence();
            default -> {
                logger.warning("Something wrong");
                updateUi();
            }
        }
    }

    private void acceptRequest() {
        viewRequests();
        if (this.scheduleBean.getAllShiftSchedules() != null){
            String chosenShift = getUserInput("Choose a schedule");
            try {
                int index = Integer.parseInt(chosenShift);
                this.scheduleBean.setShift(index);
                this.shiftManager.manageRequest(true);

            }catch (NumberFormatException e){
                outline(e.getMessage());
                String entry = getUserInput("acceptRequest: continue ? y - n");
                if (entry.equalsIgnoreCase("y") || entry.equalsIgnoreCase("yes")){
                    acceptRequest();
                }
            } catch (ShiftScheduleDaoException e) {
                outline("Some error with method shiftManager: " + e.getMessage());
            }
            updateUi();
            return;
        }
        outline("no request available");
        updateUi();
    }

    private void viewRequests() {
        this.shiftManager.getUpdateRequest();
        outline(this.scheduleBean.getAllShiftSchedules());
    }


    private void getEmployeesSchedule() {
        this.shiftManager.pullSchedules();
    }

    private void assignShift() {
        if (this.shiftCache == null){
            fillShiftCache();
        }
        parseShiftCommand(0);
        try {
            this.shiftManager.assignShift();
            outline("shift assigned");
        } catch (EmployeeDaoException | ShiftDaoException | ShiftDateException | ShiftScheduleDaoException e) {
            outline(e.getMessage());
        }finally {
            updateUi();
        }

    }

    private void parseShiftCommand(int op) {
        String entry;
        String message;
        ShiftBeanCommand command;
        switch (op) {
            case 0 -> {
                printShifts();
                message = "shift code";
                command = ShiftBeanCommand.SHIFT_CODE;
            }
            case 1 -> {
                message = "employee code";
                command = ShiftBeanCommand.EMPLOYEE_CODE;
            }
            case 2 -> {
                message = "shift date";
                command = ShiftBeanCommand.DATE_TIME;
            }
            default -> {
                return;
            }
        }
        entry = getUserInput(message);

        try {
            this.scheduleBean.insertCommand(command, entry);
        } catch (BadCommanEntryException e) {
            outline(e.getMessage());
            entry = getUserInput("continue ? y - n");
            if (entry.equalsIgnoreCase("y") || entry.equalsIgnoreCase("yes")){
                parseShiftCommand(op);
            }
        }

        if (op < 2){
            parseShiftCommand(op + 1);
        }
    }

    private void printShifts() {
        StringBuilder builder = new StringBuilder("\n");
        for (Map.Entry<Integer, String> entry:
                this.shiftCache.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        out(builder.toString());
    }

    private void fillShiftCache() {
        this.shiftCache = new HashMap<>();
        List<String> shifts = this.scheduleBean.getShifts();
        int index = 1;
        for (String s:
                shifts) {
            this.shiftCache.put(index++, s);
        }
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