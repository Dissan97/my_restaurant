package org.dissan.restaurant.gui.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.dissan.restaurant.patterns.structural.facade.TableDaoException;


public class OrderHome {

    @FXML
    public ListView<String> tableList;
    @FXML
    public Button startButton;
    @FXML
    public TextArea customers;

    private CustomerOrderFacade facade;

    @FXML
    public void initialize(){
        facade = new CustomerOrderFacade();
        updateTableList();
    }

    private void updateTableList(){
        tableList.getItems().addAll(facade.getFreeTables());
    }

    @FXML
    public void start() {
        String table = tableList.getSelectionModel().getSelectedItem();
        String customerString = customers.getText();
        int customerNumber;
        if (!customerString.isEmpty()) {
            try{
                customerNumber = Integer.parseInt(customerString);
            if (table != null) {
                    facade.setUpTable(table, customerNumber);
            }

            GuiController.openCustomerWindow(Scenes.CUSTOMER_ORDER_VIEW, facade);

            }catch (NumberFormatException | TableDaoException e){
                GuiController.popUpError(e);
                updateTableList();
            }
        }

    }
}
