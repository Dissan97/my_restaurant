package org.dissan.restaurant.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.controllers.LoginController;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;
import org.dissan.restaurant.models.UserRole;

public class SignUpGuiController {
    @FXML
    public TextArea username;
    @FXML
    public PasswordField password;
    @FXML
    public TextArea name;
    @FXML
    public TextArea surname;
    @FXML
    public TextArea cityOfBirth;
    @FXML
    public DatePicker dateOfBirth;
    @FXML
    public ChoiceBox<String> role;
    private final String[] roles = {
            UserRole.ATTENDANT.name(),
            UserRole.COOKER.name()
    };
    private LoginController controller;
    private UserBean userBean;

    @FXML
    public void initialize(){
        this.controller = new LoginController();
        this.userBean = this.controller.getUserBean();
        this.role.getItems().addAll(roles);
    }
    @FXML
    public void signUp() {
        String usr = this.username.getText();
        String pwd = this.password.getText();
        String nm = this.name.getText();
        String snm = this.surname.getText();
        String city = this.cityOfBirth.getText();
        String date = "";
        if (this.dateOfBirth.getValue() != null) {
             date = this.dateOfBirth.getValue().toString();
        }
        String rl = "" ;
        if (role != null) {
            rl = this.role.getValue();
        }
        try {
            boolean controlNotEmpty = (
                        usr.isEmpty() ||
                        pwd.isEmpty() ||
                        nm.isEmpty() ||
                        snm.isEmpty() ||
                        city.isEmpty() ||
                        date.isEmpty() ||
                        rl.isEmpty()
            );
            if (controlNotEmpty){
                throw new BadCommandEntryException("Fields cannot be empty");
            }
            this.userBean.insertCommand(UserBeanCommand.USERNAME, usr);
            this.userBean.insertCommand(UserBeanCommand.PASSWORD, pwd);
            this.userBean.insertCommand(UserBeanCommand.NAME, nm);
            this.userBean.insertCommand(UserBeanCommand.SURNAME, snm);
            this.userBean.insertCommand(UserBeanCommand.CITY_OF_BIRH, city);
            this.userBean.insertCommand(UserBeanCommand.DATE, date);
            this.userBean.insertCommand(UserBeanCommand.ROLE, rl);
            this.controller.singIn();
        } catch (BadCommandEntryException | UserCredentialException e) {
            GuiController.popUpError(e);
        }

    }
    @FXML
    public void back() {
        GuiController.getBack();
    }

    public void home() {
        GuiController.getHome();
    }
}
