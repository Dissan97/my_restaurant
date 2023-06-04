package org.dissan.restaurant.beans.api;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ShiftScheduleBeanEmployeeApi {
    void insertCommand(@NotNull ShiftBeanCommand command, String entry) throws BadCommanEntryException;
    String getShiftInfo();
    Map<Integer,String> getMyShiftSchedules();
    void clean();

}
