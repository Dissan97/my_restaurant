package org.dissan.restaurant.beans.api;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ShiftScheduleBeanApi extends ShiftScheduleBeanEmployeeApi{
    String getAllShiftSchedules();
    List<String> getShifts();
    void insertCommand(@NotNull ShiftBeanCommand command, String entry) throws BadCommanEntryException;
    void clean();


}
