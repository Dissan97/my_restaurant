package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanEmployeeApi;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.*;

import java.util.Map;

public class UpdateEmployeeShiftCliState extends CliState {
    private final ShiftManagerEmployeeApi shiftManager;
    private final ShiftScheduleBeanEmployeeApi shiftBean;

    public UpdateEmployeeShiftCliState(UserBean userBean) throws EmployeeDaoException {
        super(UpdateEmployeeShiftCliState.class.getSimpleName());
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
            case "view_schedules", "1":
                viewSchedules();
                updateUi();
                break;
            case "request_update", "2":
                requestUpdate();
                updateUi();
                break;
            case "help","3":
                showHelp();
                break;
            case "exit", "4":
                break;
            case "back", "5":
                getBack();
                break;
            default:
                logger.warning("Something wrong");
                updateUi();
                break;
        }

    }


    private void viewSchedules() {
        this.shiftManager.getMySchedule();
        outline(shiftBean.getShiftInfo());
    }

    private void requestUpdate() {
        Map<Integer, String> shiftList = shiftBean.getMyShiftSchedules();
        outline("shift schedules: " + shiftList.toString());
        String cmd = getUserInput("insert a number in the list to choose the shift schedule");
        try {
            int shiftChoose = Integer.parseInt(cmd);
            this.shiftBean.setShift(shiftChoose);
            this.shiftManager.requestUpdate();
        }catch (NumberFormatException e){
            outline("bad number don't insert negative and max available is " + shiftList.size());
        } catch (ShiftScheduleDaoException e) {
            outline("shift manager problem: " + e.getMessage());
        } finally {
            updateUi();
        }

    }

    public static void main(String[] args) throws BadCommanEntryException, UserCredentialException, EmployeeDaoException {
        LoginController controller = new LoginController();
        UserBean bean = controller.getUserBean();
        bean.insertCommand(UserBeanCommand.USERNAME, "cooker");
        bean.insertCommand(UserBeanCommand.PASSWORD, "This.is97");
        controller.singIn();

        UpdateEmployeeShiftCliState shiftCliState = new UpdateEmployeeShiftCliState(bean);
        shiftCliState.updateUi();
    }
}
