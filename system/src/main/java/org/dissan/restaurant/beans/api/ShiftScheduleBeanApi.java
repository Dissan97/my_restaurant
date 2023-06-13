package org.dissan.restaurant.beans.api;

import java.util.List;

public interface ShiftScheduleBeanApi extends ShiftScheduleBeanEmployeeApi{
    String getAllShiftUpdateScheduleRequests();
    List<String> getShiftSchedules();
    List<String> getShifts();

    List<String> getUpdateShiftSchedules();
    void setShift(String shiftCode);
}
