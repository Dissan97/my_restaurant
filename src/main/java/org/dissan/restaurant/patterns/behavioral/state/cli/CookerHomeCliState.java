package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.jetbrains.annotations.NotNull;

public class CookerHomeCliState extends AccountHomeCliState{
    protected CookerHomeCliState(String className, @NotNull UserBean userBean) {
        super(className, userBean);
    }

    @Override
    public void updateUi() {

    }
}
