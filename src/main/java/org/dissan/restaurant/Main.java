package org.dissan.restaurant;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.gui.fxml.controllers.GuiController;
import org.dissan.restaurant.cli.patterns.behavioral.state.CliState;
import org.dissan.restaurant.cli.patterns.behavioral.state.CliStateEnum;
import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.dissan.restaurant.cli.patterns.behavioral.creational.factory.StateFactory;
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
        startApp(view, args);
    }

    private static void startApp(@NotNull String view, String...args) throws CliUiException {
        if (view.equals(CLI)){
            CliState home = StateFactory.getInstance(CliStateEnum.HOME, null);
            home.updateUi();
        }else if (view.equals(GUI)){
            GuiController.launchGui(args);
        }else {
            OutStream.println("ERROR SOMETHING WRONG");
        }
    }
}
