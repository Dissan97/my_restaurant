package org.dissan.restaurant.gui.fxml.controllers;

import javafx.fxml.FXML;

public class AttendantGuiController extends AccountControllerGui {

    @FXML
    public void manageSchedules() {
        GuiController.openManageEmployeeSchedule(Scenes.MANAGE_SCHEDULES, this.userBean);
    }


}
