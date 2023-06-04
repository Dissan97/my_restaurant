package org.dissan.restaurant.beans;

import org.dissan.restaurant.beans.api.ShiftScheduleBeanApi;
import org.dissan.restaurant.models.Shift;
import org.dissan.restaurant.models.ShiftSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShiftScheduleBean implements ShiftScheduleBeanApi {

    private ShiftSchedule shiftSchedule;
    private List<ShiftSchedule> shiftScheduleList;
    private List<Shift> shiftList;
    private final EnumMap<ShiftBeanCommand, String> viewEntries = new EnumMap<>(ShiftBeanCommand.class);
    private UserBean userBean;
    private EmployeeBean employeeBean;


    public void setUpdateRequestList(List<ShiftSchedule> ssl) {
        this.shiftScheduleList = ssl;
    }

    public void setShiftList(List<Shift> sl) {
        this.shiftList = sl;
    }


    public List<ShiftSchedule> getShiftScheduleList() {
        return shiftScheduleList;
    }

    @Override
    public String getAllShiftSchedules() {
        return this.shiftSchedule.toString();
    }

    @Override
    public List<String> getShifts() {
        List<String> stringList = new ArrayList<>();
        for (Shift s:
             shiftList) {
            stringList.add("code: " + s.getCode() + " task: " + s.getTask());
        }

        return stringList;
    }

    @Override
    public void insertCommand(@NotNull ShiftBeanCommand command, String entry) throws BadCommanEntryException {
        switch (command){
            case EMPLOYEE_CODE:
            case SHIFT_CODE:
                BeanUtil.handleCommon(entry);
                break;
            case DATE_TIME:
                if (BeanUtil.goodDate(entry, true) == null){
                    throw new BadCommanEntryException(entry + "is not good date");
                }
                break;
            default:
                throw new BadCommanEntryException("entry unavailable");
        }
        this.viewEntries.remove(command);
        this.viewEntries.put(command, entry);
    }

    @Override
    public void clean() {
        this.viewEntries.clear();
    }


    @Override
    public String getShiftInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Shift schedules").append('\n');
        for (ShiftSchedule ss:
             this.shiftScheduleList) {
            builder.append(ss.toString()).append('\n');
        }
        return builder.toString();
    }

    @Override
    public Map<Integer, String> getMyShiftSchedules() {
        Map<Integer, String> mySchedules = new HashMap<>();

        int index = 0;
        for (ShiftSchedule sSchedule:
                this.shiftScheduleList) {
            mySchedules.put(index, sSchedule.toString());
        }

        return mySchedules;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setShiftScheduleList(List<ShiftSchedule> schedules) {
        this.shiftScheduleList = schedules;
    }

    public String getRelativeEntry(ShiftBeanCommand command) {
        return this.viewEntries.get(command);
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean empBean) {
        this.employeeBean = empBean;
    }
}
