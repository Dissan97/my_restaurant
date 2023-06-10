package org.dissan.restaurant.fxml.controllers;

import javafx.fxml.FXML;
import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.models.UserRole;

public class AccountControllerGui {
    protected UserBean userBean;
    protected EmployeeBean employeeBean;
    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;

        if (!this.userBean.getRole().equals(UserRole.MANAGER)){
            LoginController controller = new LoginController();
            controller.setUserBean(userBean);
            this.employeeBean = controller.getEmployeeBean(userBean);
        }
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
