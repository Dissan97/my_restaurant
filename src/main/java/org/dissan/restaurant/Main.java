package org.dissan.restaurant;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.behavioral.state.cli.CliState;
import org.dissan.restaurant.patterns.behavioral.state.cli.CliStateEnum;
import org.dissan.restaurant.patterns.behavioral.state.cli.exceptions.CliUiException;
import org.dissan.restaurant.patterns.creational.factory.StateFactory;
import org.jetbrains.annotations.NotNull;

public class Main {

    private static final String CLI = "CLI";
    private static final String GUI = "GUI";


    public static void main(String...args) throws CliUiException {
        String view;
        try{
            view = args[0];
        }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            view = CLI;
        }
        startApp(view);
    }

    private static void startApp(@NotNull String view) throws CliUiException {
        if (view.equals(CLI)){
            CliState home = StateFactory.getInstance(CliStateEnum.HOME, null);
            home.updateUi();
        }else if (view.equals(GUI)){
            //todo gui implementation
            OutStream.println("not implemented yet");
        }else {
            OutStream.println("ERROR SOMETHING WRONG");
        }
    }
}
