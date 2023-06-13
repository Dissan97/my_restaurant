package org.dissan.restaurant.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.dissan.restaurant.beans.ShiftScheduleBean;
import org.dissan.restaurant.controllers.ShiftManager;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;


public class ManageUpdatesGuiController {

    private ShiftManager shiftManager;
    private ShiftScheduleBean shiftScheduleBean;

    @FXML
    public ListView<String> updateRequestList;

    @FXML
    public void initialize(){
        this.shiftManager = new ShiftManager();
        this.shiftScheduleBean = this.shiftManager.getBean();
        updateRequests();
    }

    private void updateRequests() {
        this.updateRequestList.getItems().clear();
        this.shiftManager.getUpdateRequest();
        this.updateRequestList.getItems().addAll(this.shiftScheduleBean.getUpdateShiftSchedules());
    }

    @FXML
    public void updateShiftSchedule() {
        updateRequests();
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
        this.shiftManager.switchPersistence();
    }
    @FXML
    public void acceptRequest() {
        String shiftSelected = this.updateRequestList.getSelectionModel().getSelectedItem();
        manageShiftSchedule(shiftSelected, true);
    }

    private void manageShiftSchedule(String shiftSelected, boolean accepted) {
        shiftSelected = getShiftSelectedCode(shiftSelected);
        this.shiftScheduleBean.setShift(shiftSelected);
        try {
            this.shiftManager.manageRequest(accepted);
            String message = "Request refused";
            if (accepted){
                message = "Request accepted";
            }
            GuiController.popUpMessage(message);
        } catch (ShiftScheduleDaoException e) {
            GuiController.popUpError(e);
        }
    }

    @FXML
    public void refuse() {
        String shiftSelected = this.updateRequestList.getSelectionModel().getSelectedItem();
        manageShiftSchedule(shiftSelected, false);
    }

    private String getShiftSelectedCode(String selectedShift){
        String[] firstElement = selectedShift.split(";");
        String shiftCode = firstElement[0].split(">>")[1];
        String employeeCode = firstElement[1].split(">>")[1];
        String shiftDate = firstElement[2].split(">>")[1];
        return shiftCode + ';' + employeeCode + ';' + shiftDate;
    }
}
