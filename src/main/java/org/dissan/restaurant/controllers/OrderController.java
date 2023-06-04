package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.controllers.api.AttendantOrderApi;
import org.dissan.restaurant.controllers.api.CookerOrderApi;
import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.OrderCheck;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.check.OrderCheckDao;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderController implements  CookerOrderApi, AttendantOrderApi {

    private TableBean tableBean;
    private TableObserver observer;
    public OrderController() {
        this.tableBean = new TableBean();
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
    }


    public void sendOrder() {
        List<MealItem> currentCart = this.tableBean.getCart();
        double bill = 0.0;
        for (MealItem mi:
                currentCart) {
            bill += mi.getPrice();
        }
        OrderCheck orderCheck = this.tableBean.getCheck();
        orderCheck.setMealItems(currentCart);
        orderCheck.setBill(bill);
    }

    public void pay() {
        if (this.tableBean.acceptedPayment()){
            OrderCheckDao.storeCheck(this.getTableBean().getCheck());
        }
    }

    public TableBean getTableBean(){
        return tableBean;
    }

    public TableBean getTableBean(String tableName, int clients) {
        // TODO: 01/06/23 implement for other actors
        return tableBean;
    }

    public List<String> getFreeTables() {
        return null;
    }

    @Override
    public void setOrderReady() {

    }

    @Override
    public void isThereDelivery() {

    }

    @Override
    public void setDelivered() {

    }

    @Override
    public void checkBill() {

    }

    public void setTable(Table table) {
        OrderCheck orderCheck = new OrderCheck(table);
        this.tableBean.setCheck(orderCheck);
        this.tableBean.setTable(table);
    }

    public void setTableBean(@NotNull TableBean tableBean) {
        this.tableBean = tableBean;
        tableBean.setMealItem(MealItemDao.pullMenuItems());
        OrderCheck orderCheck = new OrderCheck(tableBean.getTable());
        tableBean.setCheck(orderCheck);
    }
}
