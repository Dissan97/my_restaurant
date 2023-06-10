package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.api.TableBeanApi;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.dissan.restaurant.patterns.structural.facade.TableDaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class OrderCliState extends CliState{

    private final CustomerOrderFacade orderController;
    private TableBeanApi tableBean;
    private Map<Integer, List<String>> itemMap;

    public OrderCliState(CliState cliState, CustomerOrderFacade facade) {
        super(OrderCliState.class.getSimpleName());
        previousState = cliState;
        this.orderController = facade;
    }

    public void initFacade() throws TableDaoException {
        this.tableBean = this.orderController.getTableBean();
    }

    @Override
    public void updateUi() {
        String cmd = getUserInput();

        if (badParseCmd(cmd)){
            updateUi();
        }

        switch (cmd) {
            case "add_item", "1" -> addItem();
            case "remove_item", "2" -> removeItem();
            case "send_order", "3" -> sendOrder();
            case "pay", "4" -> pay();
            case "help", "5" -> super.showHelp();
            case "exit", "6" -> super.exitProgram();
            default -> {
                OutStream.println("SOME PROBLEM OCCURRED");
                updateUi();
            }
        }
    }

    private void removeItem() {
        //check cart
        List<String> currentCart = this.tableBean.getCurrentCart();
        if (!currentCart.isEmpty()){
            //do stuff
            OutStream.println("to implement");
        }
        updateUi();
    }

    private void addItem() {
        if (this.itemMap == null){
            this.itemMap = this.tableBean.getItems();
        }
        printItems();
        getItemFromUser();

    }

    private void getItemFromUser() {
        String itemInput = getUserInput("choose an item");
        String item = checkItem(itemInput, itemMap);
        if (item == null){
            askContinueAddItem(true);
            return;
        }

        this.tableBean.addItem(item);
        //controller add item
        askContinueAddItem(false);
    }

    private void askContinueAddItem(boolean notFound){
        String itemInput;
        String message = "add another item";
        if (notFound){
             message = "item not present";
        }
        itemInput = getUserInput( message + " continue ? - y" );

        if (itemInput.equalsIgnoreCase("yes") || itemInput.equalsIgnoreCase("y")) {
            printItems();
            getItemFromUser();
        }else {
            updateUi();
        }

    }

    private @Nullable String checkItem(String itemInput, @NotNull Map<Integer, List<String>> itemMap) {
        try {
            return itemMap.get(Integer.valueOf(itemInput)).get(0);
        }catch (NumberFormatException e){
            for (Map.Entry<Integer, List<String>> entry:
                 itemMap.entrySet()) {
                if (entry.getValue().get(0).equals(itemInput)){
                    return itemInput;
                }
            }
        }catch (NullPointerException e){
            return null;
        }
        return null;
    }

    private void printItems() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, List<String>> entry :
             itemMap.entrySet()) {
            int i;
            List<String> info = entry.getValue();
            for (i = 0; i < info.size() - 1; i++){
                switch (i) {
                    case 0 ->
                            builder.append('[').append(entry.getKey()).append(']').append("name: ").append(info.get(i)).append("; ");
                    case 1 -> builder.append("price: ").append(info.get(i)).append('\n').append("ingredients: {");
                    default -> builder.append("price: ").append(info.get(i));
                }
            }
            builder.append(info.get(i)).append('}').append('\n');
        }
        outline("available items:\n"+ builder);

    }

    private void sendOrder() {
        this.orderController.sendOrder();
        updateUi();
    }
    private void pay() {
        this.orderController.pay();
        super.exitProgram();
    }




}
