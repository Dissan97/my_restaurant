package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.controllers.api.AttendantOrderApi;
import org.dissan.restaurant.controllers.api.CookerOrderApi;
import org.dissan.restaurant.controllers.api.CustomerOrderApi;
import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

public class OrderController implements CustomerOrderApi, CookerOrderApi, AttendantOrderApi {

    private TableBean tableBean;
    private TableObserver observer;
    public OrderController(Table table) {
        this.tableBean = new TableBean(table);
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
    }


    @Override
    public void addItem(MealItem item) {

    }

    @Override
    public void sendOrder() {

    }

    @Override
    public void pay() {

    }

    @Override
    public TableBean getMenuBean() {
        return tableBean;
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

}
