package org.dissan.restaurant.models;

import org.dissan.restaurant.beans.BeanUtil;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;

public class ShiftSchedule{
    private final Shift shift;
    private final Employee employee;
    private String shiftDate;
    private boolean updateRequest;

    public ShiftSchedule(Shift sft, Employee emp, String dTime) throws ShiftDateException {
        this(sft, emp, dTime, false);
    }

    public ShiftSchedule(Shift sft, Employee emp, String dTime, boolean upRequest) throws ShiftDateException {
        this.shift = sft;
        this.employee = emp;
        this.setShiftDate(dTime);
        this.updateRequest = upRequest;
    }

    public String getShiftDate() {
        return shiftDate;
    }



    /**
     * This method setUp correctly the date due to internal policy.
     * @param shiftDate String date parse it and set the variable this.dateTime correctly
     * @throws ShiftDateException This exception is thrown when some date is incorrect semantic
     * when date is before today and when is after a week from today
     */

    public void setShiftDate(String shiftDate) throws ShiftDateException {
        String sDate = BeanUtil.goodDate(shiftDate, true);
        if (sDate != null){
            this.shiftDate = sDate;
            return;
        }
        throw new ShiftDateException(shiftDate + "bad date");
    }

    @Override
    public String toString(){
        return this.shift.getCode() + ";" + this.employee.getEmployeeCode() + ";" + this.getShiftDate();
    }

    public void setUpdate(boolean update) {
        this.updateRequest = update;
    }

    public String getShiftCode() {
        return shift.getCode();
    }

    public String getEmployeeCode() {
        return employee.getEmployeeCode();
    }

    public boolean isUpdateRequest() {
        return this.updateRequest;
    }
}
