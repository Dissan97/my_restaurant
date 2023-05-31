package org.dissan.restaurant.fxml.controllers;

import javafx.fxml.FXML;
import org.dissan.restaurant.beans.UserBean;

public class AccountControllerGui {
    protected UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @FXML
    public void back(){
        GuiController.getBack();
    }

    @FXML
    public void home(){
        GuiController.getHome();
    }

    @FXML
    public void account() {
        GuiController.openNewWindow(Scenes.ACCOUNT, userBean);

    }
}
