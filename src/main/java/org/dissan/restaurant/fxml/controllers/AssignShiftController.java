package org.dissan.restaurant.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.dissan.restaurant.beans.api.ShiftScheduleBeanApi;
import org.dissan.restaurant.controllers.ShiftManager;

public class AssignShiftController extends AccountControllerGui{

    @FXML
    public TextArea shiftCd;
    @FXML
    public TextArea employeeCd;
    @FXML
    public DatePicker date;
    @FXML
    public ListView<String> shiftScheduleList;

    private ShiftManager shiftManager;
    private ShiftScheduleBeanApi scheduleBean;

    @FXML
    public void initialize(){
        this.shiftManager = new ShiftManager();
        this.scheduleBean = shiftManager.getBean();
        updateShiftSchedule();
    }

    @FXML
    private void updateShiftSchedule() {
        this.shiftManager.pullSchedules();
        this.shiftScheduleList.getItems().addAll(this.scheduleBean.getAllShiftSchedules());
    }

}
