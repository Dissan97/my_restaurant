package org.dissan.restaurant.gui.fxml.controllers;

import javafx.fxml.FXML;


public class HomeGui{
    @FXML
    public void login() {
        GuiController.openNewWindow(Scenes.LOGIN_VIEW);
    }

    @FXML
    public void order() {
        GuiController.openNewWindow(Scenes.ORDER_HOME);
    }

}
