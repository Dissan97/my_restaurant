package org.dissan.restaurant.models.dao.schedule;

import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.models.*;
import org.dissan.restaurant.models.dao.shift.ShiftDao;
import org.dissan.restaurant.models.dao.user.EmployeeDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShiftScheduleDao {

    private boolean local;


    public ShiftScheduleDao(boolean lcl) {
        this.local = lcl;
    }

    public ShiftScheduleDao() {
        this(true);
    }

    public void update(@NotNull ShiftSchedule schedule) {
        //to implement
    }

    public void pushShiftSchedule(@NotNull ShiftSchedule schedule) throws ShiftScheduleDaoException {
        JSONObject object = new JSONObject();
        object.put(ShiftScheduleDaoFs.SHIFT, schedule.getShiftCode());
        object.put(ShiftScheduleDaoFs.EMPLOYEE_CODE, schedule.getEmployeeCode());
        object.put(ShiftScheduleDaoFs.SHIFT_DATE, schedule.getShiftDate());
        object.put(ShiftScheduleDaoFs.UPDATE_REQUEST, schedule.isUpdateRequest());
        ShiftScheduleDaoFs.pushSchedule(object);
    }

    public List<ShiftSchedule> pullShiftSchedules(){
        List<ShiftSchedule> shiftScheduleList = new ArrayList<>();
        JSONArray array;
        if (local){
            array = ShiftScheduleDaoFs.pullShiftSchedules();
        }else {
            array = ShiftScheduleDaoDb.pullShiftSchedules();
        }



        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String shift = object.getString(ShiftScheduleDaoFs.SHIFT);
                String employeeCode = object.getString(ShiftScheduleDaoFs.EMPLOYEE_CODE);
                String shiftDate = object.getString(ShiftScheduleDaoFs.SHIFT_DATE);
                boolean updateRequest = object.getBoolean(ShiftScheduleDaoFs.UPDATE_REQUEST);
                ShiftSchedule schedule = fillSchedule(shift, employeeCode, shiftDate, updateRequest);
                shiftScheduleList.add(schedule);
            }
        }
        return shiftScheduleList;
    }

    private @Nullable ShiftSchedule fillSchedule(String sCode, String eCode, String dTime, boolean rUpdate){
        ShiftDao dao = new ShiftDao();
        Shift sft = dao.getShiftByCode(sCode);
        Employee emp = EmployeeDao.getEmployeeByCode(eCode);
        try {
            ShiftSchedule schedule = new ShiftSchedule(sft, emp, dTime);
            schedule.setUpdate(rUpdate);
            return schedule;
        } catch (ShiftDateException e) {
            return null;
        }
    }

    public List<ShiftSchedule> getShiftUpdateRequest() {
        return new ArrayList<>();
    }

    public ShiftSchedule getShiftByKey(String sCode, String eCode, String dateTime) {
        ShiftSchedule schedule = null;
        JSONObject object = null;
        if (local){
             object = ShiftScheduleDaoFs.pullSchedule(sCode, eCode, dateTime);
        }

        if (object != null){
            String shift = object.getString(ShiftScheduleDaoFs.SHIFT);
            String employeeCode = object.getString(ShiftScheduleDaoFs.EMPLOYEE_CODE);
            String shiftDate = object.getString(ShiftScheduleDaoFs.SHIFT_DATE);
            boolean updateRequest = object.getBoolean(ShiftScheduleDaoFs.UPDATE_REQUEST);
            schedule = this.fillSchedule(shift, employeeCode, shiftDate, updateRequest);
        }

        return schedule;
    }


    //todo remove this main
    public static void main(String[] args) throws ShiftDateException, ShiftScheduleDaoException {
        Shift shift = new Shift("12346");
        AbstractUser user = new ConcreteUser();
        Employee emp = new Attendant(user);
        emp.setEmployeeCode("4321");
        ShiftSchedule schedule = new ShiftSchedule(shift, emp, "01-05-2023::10:30");
        ShiftScheduleDao me = new ShiftScheduleDao();
        me.pushShiftSchedule(schedule);
    }
}
