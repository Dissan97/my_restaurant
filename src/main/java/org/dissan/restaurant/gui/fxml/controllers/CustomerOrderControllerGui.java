package org.dissan.restaurant.gui.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.dissan.restaurant.beans.api.TableBeanApi;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CustomerOrderControllerGui {
    @FXML
    public ListView<String> availableMealList;
    @FXML
    public ListView<String> cartMealList;
    @FXML
    public Label orderStatus;
    private CustomerOrderFacade orderFacade;
    private TableBeanApi tableBean;

    private Map<Integer, List<String>> mealItemMap;
    public void setFacade(@NotNull CustomerOrderFacade facade) {
        this.orderFacade = facade;
        this.tableBean = facade.getTableBean();
        showAvailableMeals();
    }

    private void showAvailableMeals(){
        mealItemMap = this.tableBean.getMealItems();
        for (Map.Entry<Integer, List<String>> entry:
        mealItemMap.entrySet()) {
            this.availableMealList.getItems().add(entry.getKey()+":"+entry.getValue().get(0));
        }
    }

    @FXML
    public void sendOrder() {
        List<String> myCart = this.cartMealList.getItems();
        if(!myCart.isEmpty()){
            for (String mealItem:
                 myCart) {
                this.tableBean.addItem(mealItem.split(":")[1]);
            }
        }
        this.orderFacade.sendOrder();
    }

    @FXML
    public void addToChart() {
        int index = this.availableMealList.getSelectionModel().getSelectedIndex() + 1;
        this.cartMealList.getItems().add(index + ":" +mealItemMap.get(index).get(0));
    }

    @FXML
    public void removeItemFromCart() {
        int index = this.cartMealList.getSelectionModel().getSelectedIndex();
        this.cartMealList.getItems().remove(index);
    }

    @FXML
    public void requestBill() {
        this.orderFacade.pay();
    }
}
