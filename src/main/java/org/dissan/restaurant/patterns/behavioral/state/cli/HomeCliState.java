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
            case "login":
            case"1":
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
            case"2":
                super.showHelp();
                this.updateUi();
                break;
            case "exit":
            case"3":
                super.exitProgram();
                break;
            default:
                OutStream.println("SOME PROBLEM OCCURRED");
                updateUi();
        }
    }

}
