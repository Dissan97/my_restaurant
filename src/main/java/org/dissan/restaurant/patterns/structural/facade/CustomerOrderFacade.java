package org.dissan.restaurant.patterns.structural.facade;


import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.OrderController;
import org.dissan.restaurant.controllers.api.CustomerOrderApi;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.ConcreteTableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;
import org.dissan.restaurant.patterns.creational.factory.ObserverFactory;
import org.dissan.restaurant.patterns.creational.factory.TableActor;
import java.util.List;

public class CustomerOrderFacade implements CustomerOrderApi {
    private TableBean tableBean;
    private final OrderController orderController;
    private TableSubject subject = null;
    private TableObserver observer;

    public CustomerOrderFacade() {
        orderController = new OrderController();
        this.tableBean = orderController.getTableBean();
    }


    public TableBean getMenuBean() {
        return tableBean;
    }

    @Override
    public String printFreeTables(){
        StringBuilder builder = new StringBuilder();
        for (String s:
        TableSubject.getFreeTables()) {
            builder.append(s).append('\n');
        }
        return builder.toString();
    }

    private void newCustomers(String tableName, int clients) throws TableDaoException {
        if (!TableSubject.getFreeTables().contains(tableName)){
            throw new TableDaoException(tableName + " occupied or not exist");
        }
        this.subject = ConcreteTableSubject.getSubject(tableName);
        TableBean bean = subject.getTableBean();
        this.orderController.setTableBean(bean);
        bean.getTable().setCustomers(clients);
        this.observer = ObserverFactory.getInstance(TableActor.CUSTOMER, tableName);
        this.subject.attach(observer);
        this.tableBean = bean;
    }

    public void sendOrder() {
        this.orderController.sendOrder();
        this.subject.notifyObservers(this.observer, TableSubjectStates.NEW_ORDER);
    }

    @Override
    public void pay() {
        if (this.getMenuBean() == null){
            OutStream.println("bean = null");
        }
        if (this.getMenuBean().getTable() == null){
            OutStream.println("table = null");
        }
        this.subject.notifyObservers(this.observer, TableSubjectStates.PAY_REQUEST);
        //simulate acceptance...
        this.tableBean.setValidPay(true);
        this.orderController.pay();
    }


    public TableBean getTableBean() {
        return this.tableBean;
    }

    @Override
    public List<String> getFreeTables() {
        return TableSubject.getFreeTables();
    }

    public void setUpTable(String tableName, int customerNumber) throws TableDaoException {
        this.newCustomers(tableName, customerNumber);
    }
}


