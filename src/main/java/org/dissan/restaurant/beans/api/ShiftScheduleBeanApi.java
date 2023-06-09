package org.dissan.restaurant.beans.api;

import java.util.List;

public interface ShiftScheduleBeanApi extends ShiftScheduleBeanEmployeeApi{
    String getAllShiftSchedules();
    List<String> getShiftSchedules();
    List<String> getShifts();

    List<String> getUpdateShiftSchedules();
    public void setShift(String shiftCode);
}
