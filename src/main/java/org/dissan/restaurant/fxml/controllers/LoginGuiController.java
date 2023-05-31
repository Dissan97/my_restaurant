package org.dissan.restaurant.fxml.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;

public class LoginGuiController {


    @FXML
    public PasswordField password;
    @FXML
    public TextArea username;
    private LoginController loginController;
    private UserBean userBean;
    @FXML
    public void initialize(){
        loginController = new LoginController();
        userBean = loginController.getUserBean();
    }

    @FXML
    public void signIn() {
        String usr = username.getText();
        String pwd = password.getText();
        if (! (usr.isEmpty() || pwd.isEmpty())){
            try {
                this.userBean.insertCommand(UserBeanCommand.USERNAME, usr);
                this.userBean.insertCommand(UserBeanCommand.PASSWORD, pwd);
                this.loginController.singIn();
                startProperUi();
            } catch (BadCommanEntryException | UserCredentialException e) {
                GuiController.popUpError(e);
            }
        }
    }

    private void startProperUi() {
        Scenes scene;
        switch (this.userBean.getRole()){
            case ATTENDANT:
                scene = Scenes.ATTENDANT_VIEW;
                break;
            case COOKER:
                scene = Scenes.COOKER_VIEW;
                break;
            case MANAGER:
                scene = Scenes.MANAGER_VIEW;
                break;
            default:
                scene = null;
                break;
        }

        if (scene != null){
            GuiController.openNewWindow(scene, this.userBean);
        }
    }

    @FXML
    public void signUp() {
        GuiController.openNewWindow(Scenes.SIGN_UP_VIEW);
    }

    public void back() {
        GuiController.getBack();
    }

    public void home() {
        GuiController.getHome();
    }
}
