package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.cli.patterns.behavioral.creational.factory.StateFactory;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.dissan.restaurant.patterns.structural.facade.TableDaoException;

import java.util.List;

public class HomeCliState extends CliState{
    public HomeCliState() {
        super(HomeCliState.class.getSimpleName());
    }
    @Override
    public void updateUi() {
        String cmd = super.getUserInput();
        if (badParseCmd(cmd)){
            updateUi();
        }
        CliState state;
        switch (cmd) {
            case "order", "1" -> selectTable();
            case "login", "2" -> {
                try {
                    state = StateFactory.getInstance(CliStateEnum.LOGIN, this);
                } catch (CliUiException e) {
                    outline("cli exception catch");
                    updateUi();
                    return;
                }
                this.changeState(state);
            }
            case "help", "3" -> super.showHelp();
            case "exit", "4" -> super.exitProgram();
            default -> {
                OutStream.println("SOME PROBLEM OCCURRED");
                updateUi();
            }
        }
    }

    private void printTables(List<String> freeTables){
        StringBuilder builder = new StringBuilder("Free tables\n");
        for (String ft:
             freeTables) {
            builder.append("Table name-> [").append(ft).append("]\n");
        }
        out(builder.toString());
    }

    private void selectTable() {
        CustomerOrderFacade facade = new CustomerOrderFacade();
        OrderCliState orderCliState = new OrderCliState(this, facade);
        List<String> freeTables = facade.getFreeTables();
        printTables(freeTables);
        parseTableCmd(facade);
        orderCliState.initFacade();
        orderCliState.updateUi();
    }

    private void parseTableCmd(CustomerOrderFacade facade){
        String tableName = getUserInput("Table name");
        int customerNumber;
        try {
            customerNumber = Integer.parseInt(getUserInput("Customers: "));
        }catch (NumberFormatException e){
            badTableCmd("This is not a number", facade);
            return;
        }

        if (customerNumber <= 0){
            badTableCmd("Cannot insert negative number", facade);
        }

        if (!facade.getFreeTables().contains(tableName)){
            badTableCmd("Table does not exist or is not free", facade);
        }

        try {
            facade.setUpTable(tableName, customerNumber);
        } catch (TableDaoException e) {
            badTableCmd("something wrong", facade);
        }
    }

    private void badTableCmd(String message, CustomerOrderFacade facade){
        outline(message);
        String cmd = getUserInput("Want to continue ? y - n");
        if (cmd.equalsIgnoreCase("yes") || cmd.equalsIgnoreCase("y")){
            parseTableCmd(facade);
        }
    }



}
