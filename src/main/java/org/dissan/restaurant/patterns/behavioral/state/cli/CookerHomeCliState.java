package org.dissan.restaurant.patterns.behavioral.state.cli;

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

        switch (cmd){
            case "view_order":
            case "1":
            case "set_order_ready_to_deliver":
            case "2":
                //not implemented
                updateUi();
                break;
            case REQUEST_UPDATE:
                viewMySchedules();
                parseRequestInput(0);
                updateUi();
                break;
            case "exit":
                exitProgram();
                break;
            default:
                logger.warning("Something wrong");
                updateUi();
                break;
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
