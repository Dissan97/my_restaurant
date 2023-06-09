package org.dissan.restaurant.fxml.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanApi;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;

import java.util.List;

public class AssignShiftGuiController extends AccountControllerGui{

    @FXML
    public TextArea shiftCd;
    @FXML
    public TextArea employeeCd;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ListView<String> shiftScheduleList;
    @FXML
    public MenuButton hourMenuButton;
    @FXML
    public Label hour;

    private ShiftManager shiftManager;
    private ShiftScheduleBeanApi scheduleBean;

    @FXML
    public void initialize(){
        this.shiftManager = new ShiftManager();
        this.scheduleBean = shiftManager.getBean();
        ObservableList<MenuItem> items = this.hourMenuButton.getItems();
        for (MenuItem i:
             items) {
            i.setOnAction(actionEvent -> hour.setText(i.getText()));
        }
        updateShiftSchedule();
    }

    @FXML
    private void updateShiftSchedule() {
        this.shiftScheduleList.getItems().clear();
        this.shiftManager.pullSchedules();
        List<String> shiftSchedules = this.scheduleBean.getShiftSchedules();
        if (!shiftSchedules.isEmpty()) {
            this.shiftScheduleList.getItems().addAll(shiftSchedules);
        }else {
            this.shiftScheduleList.getItems().add("No shift available");
        }
    }

    @FXML
    public void assignShift() {
        String shiftDate = this.datePicker.getEditor().getText();
        String employeeCode = this.employeeCd.getText();
        String shiftCode = this.shiftCd.getText();
        String shiftHour = this.hour.getText();
        if (!(shiftDate.isEmpty() || employeeCode.isEmpty() || shiftCode.isEmpty() || shiftHour.isEmpty())){
            try {
                this.scheduleBean.insertCommand(ShiftBeanCommand.DATE_TIME, shiftDate + "::" + shiftHour);
                this.scheduleBean.insertCommand(ShiftBeanCommand.EMPLOYEE_CODE, employeeCode);
                this.scheduleBean.insertCommand(ShiftBeanCommand.SHIFT_CODE, shiftCode);
                this.shiftManager.assignShift();
                GuiController.popUpMessage("schedule added");
            } catch (BadCommandEntryException | EmployeeDaoException | ShiftDaoException | ShiftDateException |
                     ShiftScheduleDaoException e) {
                    GuiController.popUpError(e);
            }
        }
    }

    public void switchPersistence() {
        this.shiftManager.switchPersistence();
    }
}
