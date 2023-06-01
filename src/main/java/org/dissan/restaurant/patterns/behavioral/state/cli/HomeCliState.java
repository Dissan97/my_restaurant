package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.behavioral.state.cli.exceptions.CliUiException;
import org.dissan.restaurant.patterns.creational.factory.StateFactory;

public class HomeCliState extends CliState{
    public HomeCliState() {
        super(HomeCliState.class.getSimpleName());
    }
    @Override
    public void updateUi() {
        String cmd = super.getUserInput();
        if (parseCmd(cmd)){
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

    //todo Some controller that shows me the available table... --->
    private void selectTable() {

    }

}
