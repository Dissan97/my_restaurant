package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.behavioral.state.cli.exceptions.CliUiException;
import org.dissan.restaurant.patterns.creational.factory.StateFactory;
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
        switch (cmd){
            case "order":
            case "1":
                selectTable();
                break;
            case "login":
            case"2":
                try {
                    state = StateFactory.getInstance(CliStateEnum.LOGIN, this);
                } catch (CliUiException e) {
                    outline("cli exception catch");
                    updateUi();
                    return;
                }
                this.changeState(state);
                break;
            case "help":
            case"3":
                super.showHelp();
                break;
            case "exit":
            case"4":
                super.exitProgram();
                break;
            default:
                OutStream.println("SOME PROBLEM OCCURRED");
                updateUi();
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

    //todo Some controller that shows me the available table... --->
    private void selectTable() {
        CustomerOrderFacade facade = new CustomerOrderFacade();
        OrderCliState orderCliState = new OrderCliState(this, facade);
        List<String> freeTables = facade.getFreeTables();
        printTables(freeTables);
        parseTableCmd(facade);
        try {
            orderCliState.initFacade();
        } catch (TableDaoException e) {
            outline(e.getMessage());
        }
        orderCliState.updateUi();

    }

    private void parseTableCmd(CustomerOrderFacade facade){
        String tableName = getUserInput("Table name");
        int customerNumber = 0;
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
