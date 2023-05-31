package org.dissan.restaurant.patterns.behavioral.state.cli;

import org.dissan.restaurant.beans.UserBean;
import org.jetbrains.annotations.NotNull;

public class CookerHomeCliState extends EmployeeHomeCliState{
    protected CookerHomeCliState(String className, @NotNull UserBean userBean) {
        super(CookerHomeCliState.class.getSimpleName(), userBean);
    }

    @Override
    public void updateUi() {

    }
}
