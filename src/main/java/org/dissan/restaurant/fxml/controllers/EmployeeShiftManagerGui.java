package org.dissan.restaurant.fxml.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.BeanUtil;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanEmployeeApi;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;

import java.util.Map;

public class EmployeeShiftManagerGui {
    @FXML
    public ListView<String> shiftScheduleList;
    @FXML
    public DatePicker datePicker;
    @FXML
    public MenuButton hourMenuButton;
    @FXML
    public Label hour;


    private ShiftManagerEmployeeApi shiftManager;
    private ShiftScheduleBeanEmployeeApi scheduleBean;
    private Map<Integer, String> localShiftMap;

    @FXML
    public void initialize(){
        ObservableList<MenuItem> items = this.hourMenuButton.getItems();
        for (MenuItem i:
                items) {
            i.setOnAction(actionEvent -> hour.setText(i.getText()));
        }
    }

    public void setUserBean(UserBean userBean) throws EmployeeDaoException {
        this.shiftManager = new ShiftManager(userBean);
        scheduleBean = this.shiftManager.getBean();
        this.updateShiftSchedule();
    }


    @FXML
    public void home() {
        GuiController.getHome();
    }


    @FXML
    public void back() {
        GuiController.getBack();
    }

    @FXML
    public void switchPersistence() {
        shiftManager.switchPersistence();
    }

    @FXML
    public void updateShiftSchedule() {
        shiftScheduleList.getItems().clear();
        updateMyShiftSchedules();
    }

    private void updateMyShiftSchedules(){
        this.shiftManager.getMySchedule();
        this.localShiftMap = this.scheduleBean.getMyShiftSchedules();
        this.shiftScheduleList.getItems().addAll(this.localShiftMap.values());
    }
    @FXML
    public void requestUpdate() {
        String scheduleCode = this.shiftScheduleList.getSelectionModel().getSelectedItem();
        String shiftDate = this.datePicker.getEditor().getText();
        shiftDate = BeanUtil.goodDate(shiftDate, false);
        String shiftHour = this.hour.getText();
        if ( !(shiftDate == null || scheduleCode == null || shiftHour.isEmpty())){
            int index = this.getShiftIndex(scheduleCode);
            OutStream.println("Shift update date: " + this.datePicker.getEditor().getText() + " real one: " + shiftDate);
            OutStream.println("shiftcode: " + scheduleCode + "\nindex selected: " + index + "\nexpected value: " + localShiftMap.get(index));
            if (index != -1) {
                try {
                    this.scheduleBean.setShift(index);
                    this.scheduleBean.insertCommand(ShiftBeanCommand.UPDATE_DATE_TIME, shiftDate + "::" + shiftHour);
                    this.shiftManager.requestUpdate();
                    GuiController.popUpMessage("Shift request updated");
                } catch (ShiftScheduleDaoException | BadCommanEntryException e) {
                    GuiController.popUpError(e);
                }
            }
        }


    }

    private int getShiftIndex(String shiftCode) {
        for (Map.Entry<Integer, String> entry:
             this.localShiftMap.entrySet()) {
            if (entry.getValue().equals(shiftCode)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
