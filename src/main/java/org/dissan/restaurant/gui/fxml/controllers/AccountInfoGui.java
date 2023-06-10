package org.dissan.restaurant.gui.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountInfoGui extends AccountControllerGui{

    @FXML
    public Label name;
    @FXML
    public Label surname;
    @FXML
    public Label city;
    @FXML
    public Label date;
    @FXML
    public Label username;
    @FXML
    public Label role;

    public void showAccount(){
        this.name.setText(this.name.getText() + ' ' + userBean.getName());
        this.surname.setText(this.surname.getText() + ' ' + userBean.getSurname());
        this.city.setText(this.city.getText() + ' '  + userBean.getCityOfBirth());
        this.date.setText(this.date.getText() + ' ' + userBean.getDateOfBirth());
        this.role.setText(this.role.getText() + ' ' + userBean.getRole().name());
        this.username.setText(this.username.getText() + "@" + userBean.getUsername());
    }
}
