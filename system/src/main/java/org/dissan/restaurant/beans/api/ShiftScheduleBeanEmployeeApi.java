package org.dissan.restaurant.beans.api;

import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.ShiftBeanCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ShiftScheduleBeanEmployeeApi {
    void insertCommand(@NotNull ShiftBeanCommand command, String entry) throws BadCommandEntryException;
    String getShiftInfo();
    Map<Integer,String> getMyShiftSchedules();
    void setShift(int shiftChoose);
    EmployeeBean getEmployeeBean();
}
