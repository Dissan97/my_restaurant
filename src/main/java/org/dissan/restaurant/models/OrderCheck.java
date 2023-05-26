package org.dissan.restaurant.models;

import java.util.ArrayList;
import java.util.List;

public class OrderCheck {

    private final Table table;
    private double bill;
    private final List<MealItem> items;


    public OrderCheck(Table table) {
        this.table = table;
        this.bill = 0.0;
        this.items = new ArrayList<>();
    }

    public void addItem(MealItem item){
        this.items.add(item);
        this.setBill(item.getPrice());
    }

    public void removeItem(MealItem item){
        this.items.remove(item);
        this.setBill(-(item.getPrice()));
    }

    private void setBill(double price){
        this.bill += price;
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
}
