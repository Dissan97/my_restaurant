package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.controllers.api.AttendantOrderApi;
import org.dissan.restaurant.controllers.api.CookerOrderApi;
import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.OrderCheck;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.UserRole;
import org.dissan.restaurant.models.dao.check.OrderCheckDao;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.ConcreteTableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubject;
import org.dissan.restaurant.patterns.creational.factory.ObserverFactory;
import org.dissan.restaurant.patterns.creational.factory.TableActor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderController implements  CookerOrderApi, AttendantOrderApi {

    private TableBean tableBean;
    private TableObserver observer = null;
    public OrderController() {
        this(null, null );
    }

    public OrderController(String tableName, UserRole role){
        this.tableBean = new TableBean();
        if (tableName != null && role != null){
            setObserver(tableName,role);
        }
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
    }

    private void setObserver(String tableName ,UserRole role) {
        TableSubject subject = ConcreteTableSubject.getSubject(tableName);
        this.tableBean  = getTableBean();
        TableActor actor = null;

        if (role == UserRole.ATTENDANT){
            actor = TableActor.ATTENDANT;
        }

        if (role == UserRole.COOKER){
            actor = TableActor.COOKER;
        }
        assert actor != null;
        this.observer = ObserverFactory.getInstance(actor, subject.getTableBean().getTableName());
        subject.attach(observer);
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

    @Override
    public TableObserver getObserver() {
        return this.observer;
    }

    public TableBean getTableBean(String tableName, int clients) {
        return tableBean;
    }


    @Override
    public void setOrderReady() {

    }



    @Override
    public void setDelivered() {

    }

    @Override
    public void checkBill() {

    }

}
