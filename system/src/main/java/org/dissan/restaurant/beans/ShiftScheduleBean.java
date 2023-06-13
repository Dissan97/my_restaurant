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
    private EmployeeBean employeeBean;


    public void setUpdateRequestList(List<ShiftSchedule> ssl) {
        this.shiftScheduleList = ssl;
    }

    public void setShiftList(List<Shift> sl) {
        this.shiftList = sl;
    }



    @Override
    public String getAllShiftUpdateScheduleRequests() {
        final String noShifts = "no shift available";
        if (this.shiftScheduleList != null){

            if (shiftScheduleList.isEmpty()){
                return noShifts;
            }
            StringBuilder builder = new StringBuilder();

            int i;

            for (i = 0; i < shiftScheduleList.size() - 1; i++) {
                builder.append('[').append(i).append("]: ").append(shiftScheduleList.get(i).toString()).append('\n').
                append("update_date: ").append(shiftScheduleList.get(i).getShiftUpdateDate()).append('\n');
            }

            builder.append('[').append(i).append("]: ").append(shiftScheduleList.get(i).toString()).append('\n').
                    append("update_date: ").append(shiftScheduleList.get(i).getShiftUpdateDate());

            return builder.toString();
        }

        return noShifts;
    }

    @Override
    public List<String> getShiftSchedules() {
        List<String> scheduleList = new ArrayList<>();
        if (this.shiftScheduleList != null) {
            for (ShiftSchedule ss :
                    this.shiftScheduleList) {
                scheduleList.add(ss.toString());
            }
        }
        return scheduleList;
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
    public List<String> getUpdateShiftSchedules() {
        List<String> updateList = new ArrayList<>();
        for (ShiftSchedule ss:
             shiftScheduleList) {
            updateList.add("code>>" + ss.getShiftCode() + ";employee>>" + ss.getEmployeeCode() + ";shiftDate>>"
            + ss.getShiftDate() + ";new update date>>" + ss.getShiftUpdateDate()
                    );
        }
        return updateList;
    }

    @Override
    public void insertCommand(@NotNull ShiftBeanCommand command, String entry) throws BadCommandEntryException {
        switch (command) {
            case EMPLOYEE_CODE, SHIFT_CODE -> BeanUtil.handleCommon(entry);
            case DATE_TIME -> {
                if (BeanUtil.goodDate(entry, true) == null) {
                    throw new BadCommandEntryException(entry + "is not good date");
                }
            }
            case UPDATE_DATE_TIME -> {
                if (BeanUtil.goodDate(entry, true) == null) {
                    throw new BadCommandEntryException(entry + "is not good date");
                }
                if (entry.equals(this.shiftSchedule.getShiftDate())){
                    throw new BadCommandEntryException(entry + "Cannot pass the same date");
                }
            }
            default -> throw new BadCommandEntryException(command.name() + " entry unavailable");
        }
        this.viewEntries.remove(command);
        this.viewEntries.put(command, entry);
    }


    @Override
    public void setShift(int shiftChoose) {
        this.shiftSchedule = this.shiftScheduleList.get(shiftChoose);
    }

    @Override
    public void setShift(String shiftCode){
        for (ShiftSchedule ss:
             this.shiftScheduleList) {
            String codeToCompare = ss.toString();
            if (shiftCode.equals(codeToCompare)){
                this.shiftSchedule = ss;
            }
        }
    }

    public ShiftSchedule getShiftSchedule() {
        return shiftSchedule;
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

    public void setShiftScheduleList(List<ShiftSchedule> schedules) {
        this.shiftScheduleList = schedules;
    }

    public String getRelativeEntry(ShiftBeanCommand command) {
        return this.viewEntries.get(command);
    }

    @Override
    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean empBean) {
        this.employeeBean = empBean;
    }

}