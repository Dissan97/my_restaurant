package org.dissan.restaurant.fxml.controllers;


import javafx.fxml.FXML;

public class ManagerGuiController extends AccountControllerGui{

    @FXML
    public void assignShift() {
        GuiController.openNewWindow(Scenes.SHIFT_ASSIGN);
    }

    @FXML
    public void viewRequests() {
        GuiController.openNewWindow(Scenes.VIEW_UPDATES);
    }
}
//This.is97