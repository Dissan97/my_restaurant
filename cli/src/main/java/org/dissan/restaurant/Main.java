package org.dissan.restaurant;


import org.dissan.restaurant.cli.patterns.behavioral.state.CliState;
import org.dissan.restaurant.cli.patterns.behavioral.state.CliStateEnum;
import org.dissan.restaurant.cli.patterns.behavioral.state.exceptions.CliUiException;
import org.dissan.restaurant.cli.patterns.behavioral.creational.factory.StateFactory;
public class Main {



    public static void main(String...args) throws CliUiException {
        CliState home = StateFactory.getInstance(CliStateEnum.HOME, null);
        home.updateUi();
    }

}
