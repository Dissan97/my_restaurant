package org.dissan.restaurant.patterns.structural.facade;


import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.beans.api.TableBeanApi;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.OrderController;
import org.dissan.restaurant.controllers.api.CustomerOrderApi;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.ConcreteTableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;
import org.dissan.restaurant.patterns.creational.factory.ObserverFactory;
import org.dissan.restaurant.patterns.creational.factory.TableActor;
import java.util.List;
import java.util.Map;

public class CustomerOrderFacade implements CustomerOrderApi {
    private final TableBean tableBean;
    private final OrderController orderController;
    private TableSubject subject = null;
    private TableObserver observer;

    public CustomerOrderFacade() {
        orderController = new OrderController();
        this.tableBean = orderController.getTableBean();
    }

    private void pullItems(){
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
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

    //TODO DOWNLOAD TABLE HERE FOR SUBJECT SELECTION
    private TableBean newCustomers(String tableName, int clients) throws TableDaoException {
        if (!TableSubject.getFreeTables().contains(tableName)){
            throw new TableDaoException(tableName + " occupied or not exist");
        }
        this.subject = ConcreteTableSubject.getSubject(tableName);
        TableBean bean = subject.getTableBean();
        this.orderController.setTableBean(bean);
        bean.getTable().setCustomers(clients);
        this.observer = ObserverFactory.getInstance(TableActor.CUSTOMER, tableName);
        this.subject.attach(observer);
        return this.orderController.getTableBean();
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
        this.orderController.pay();
    }

    @Override
    public TableBean getTableBean(String tableName, int clients) throws TableDaoException {
        return newCustomers(tableName, clients);
    }

    @Override
    public List<String> getFreeTables() {
        return TableSubject.getFreeTables();
    }

    // TODO: 31/05/23 move this static methods to OrderCliState
    private static void printMeals(TableBeanApi tableBean){
        for (Map.Entry<Integer, List<String>> entry:
                tableBean.getItems().entrySet()) {
            List<String> meals = entry.getValue();
            StringBuilder builder = new StringBuilder();
            builder.append('[').append(entry.getKey()).append("]-> ");
            builder.append(meals.get(0)).append(" price: ").append(meals.get(1)).append("\ningredients:\t{");
            int size = meals.size();
            List<String> sub = meals.subList(2, size - 1);
            for (String m:
                    sub) {
                builder.append(m).append(", ");
            }
            builder.append(meals.get(size - 1)).append('}');
            OutStream.println(builder.toString());
        }
    }

    private static void printCurrentCart(TableBeanApi tableBean){
        StringBuilder builder = new StringBuilder();
        for (String s:
                tableBean.getCurrentCart()) {
            builder.append(s).append('\n');
        }

        OutStream.print(builder.toString());
    }

    public static void main(String[] args) throws TableDaoException {
        CustomerOrderApi facade = new CustomerOrderFacade();
        OutStream.print(facade.printFreeTables());
        TableBeanApi tableBean = facade.getTableBean("1", 5);
        OutStream.println(tableBean.getTableInfo());
        TableObserver cookerObs = ObserverFactory.getInstance(TableActor.COOKER, "1");
        TableObserver waiterObs = ObserverFactory.getInstance(TableActor.ATTENDANT, "1");
        TableSubject sbj = ConcreteTableSubject.getSubject("1");
        Table table = sbj.getTableBean().getTable();
        sbj.attach(cookerObs);
        sbj.attach(waiterObs);
        printMeals(tableBean);
        tableBean.addItem("1");
        tableBean.addItem("CARBONARA");
        facade.sendOrder();
        facade.pay();

    }

    public void setUpTable(String tableName, int customerNumber) throws TableDaoException {
        this.newCustomers(tableName, customerNumber);
    }
}


