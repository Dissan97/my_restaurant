package org.dissan.restaurant.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderCheck {

    private final Table table;
    private double bill;
    private List<MealItem> mealItems;
    private String dateTime;

    public OrderCheck(Table table) {
        this.table = table;
        this.bill = 0.0;
        this.setDateTime();
    }

    private void setDateTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy::HH:mm");
        this.dateTime = formatter.format(date);
    }


    public void setBill(double amount){
        this.bill = amount;
    }

    public double getBill() {
        return bill;
    }

    public List<MealItem> getMealItems() {
        return mealItems;
    }

    public Table getTable() {
        return table;
    }

    public void setMealItems(List<MealItem> currentCart) {
        this.mealItems = currentCart;
    }

    public String getDateTime() {
        return dateTime;
    }
}
