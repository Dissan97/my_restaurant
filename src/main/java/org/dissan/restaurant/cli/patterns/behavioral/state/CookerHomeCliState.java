package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.beans.UserBean;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CookerHomeCliState extends EmployeeHomeCliState{
    protected CookerHomeCliState(@NotNull UserBean userBean) {
        super(CookerHomeCliState.class.getSimpleName(), userBean);
    }

    @Override
    public void updateUi() {
        String cmd = super.getUserInput();
        if (super.badParseCmd(cmd)){
            updateUi();
            return;
        }

        switch (cmd) {
            case "view_order", "1", "set_order_ready_to_deliver", "2" ->
                //not implemented
                    updateUi();
            case REQUEST_UPDATE -> {
                viewMySchedules();
                parseRequestInput(0);
                updateUi();
            }
            case "exit" -> exitProgram();
            default -> {
                logger.warning("Something wrong");
                updateUi();
            }
        }

    }

    private void viewMySchedules() {
        this.shiftManager.getMySchedule();
        Map<Integer, String> scheduleMap = this.shiftBean.getMyShiftSchedules();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, String> entry:
             scheduleMap.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        }
        out(builder.toString());
    }
}
