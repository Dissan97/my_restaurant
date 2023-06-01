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
import org.dissan.restaurant.patterns.behavioral.state.cli.CliState;
import org.dissan.restaurant.patterns.behavioral.state.cli.HomeCliState;
import org.dissan.restaurant.patterns.behavioral.state.cli.OrderCliState;
import org.dissan.restaurant.patterns.creational.factory.ObserverFactory;
import org.dissan.restaurant.patterns.creational.factory.TableActor;
import java.util.List;
import java.util.Map;

public class CustomerOrderFacade implements CustomerOrderApi {
    private final TableBean tableBean;
    private final OrderController orderController;
    private CliState currentState;
    private final TableSubject subject;
    private TableObserver observer;

    public CustomerOrderFacade(CliState cliState) {
        this.currentState = new OrderCliState(cliState, this);
        subject = ConcreteTableSubject.getSubject();
        orderController = new OrderController();
        this.tableBean = orderController.getTableBean();
    }



    private void pullItems(){
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
    }

    public TableBean getMenuBean() {
        return null;
    }

    @Override
    public String printFreeTables(){
        StringBuilder builder = new StringBuilder();
        for (String s:
        subject.getFreeTables()) {
            builder.append(s).append('\n');
        }
        return builder.toString();
    }

    public TableBean newCustomers(String tableName, int clients) throws TableDaoException {
        if (!this.subject.getFreeTables().contains(tableName)){
            throw new TableDaoException(tableName + " occupied or not exist");
        }
        Table table = new Table(tableName, clients);
        this.orderController.setTable(table);
        this.observer = ObserverFactory.getInstance(TableActor.CUSTOMER);
        this.subject.attach(table, observer);
        return this.orderController.getTableBean();
    }

    public void sendOrder() {
        this.orderController.sendOrder();
        this.subject.notifyObservers(this.tableBean.getTable(),this.observer, TableSubjectStates.NEW_ORDER);
    }

    @Override
    public void pay() {
        this.orderController.pay();
        this.subject.notifyObservers(this.getMenuBean().getTable(), this.observer, TableSubjectStates.PAY_REQUEST);
    }

    @Override
    public TableBean getTableBean(String tableName, int clients) throws TableDaoException {
        return newCustomers(tableName, clients);
    }

    @Override
    public List<String> getFreeTables() {
        return null;
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
        HomeCliState homeCliState = new HomeCliState();
        CustomerOrderApi facade = new CustomerOrderFacade(homeCliState);
        OutStream.print(facade.printFreeTables());
        TableBeanApi tableBean = facade.getTableBean("1", 5);
        OutStream.println(tableBean.getTableInfo());
        TableObserver cookerObs = ObserverFactory.getInstance(TableActor.COOKER);

        TableSubject sbj = ConcreteTableSubject.getSubject();
        Table table = sbj.getRelativeTable("1").getTable();
        sbj.attach(table, cookerObs);
        printMeals(tableBean);
        tableBean.addItem("1");
        tableBean.addItem("CARBONARA");
        printMeals(tableBean);
        facade.sendOrder();

    }

}


