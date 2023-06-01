package org.dissan.restaurant.models;

import java.util.List;

public class OrderCheck {

    private final Table table;
    private double bill;
    private List<MealItem> items;

    public OrderCheck(Table table) {
        this.table = table;
        this.bill = 0.0;
    }


    public void setBill(double amount){
        this.bill = amount;
    }

    public double getBill() {
        return bill;
    }

    public List<MealItem> getItems() {
        return items;
    }

    public Table getTable() {
        return table;
    }

    public void setItems(List<MealItem> currentCart) {
        this.items = currentCart;
    }
}
