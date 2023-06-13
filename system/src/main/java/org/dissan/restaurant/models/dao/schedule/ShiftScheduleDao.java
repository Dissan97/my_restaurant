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
    protected static final  String SHIFT = "shift";
    protected static final  String EMPLOYEE_CODE = "employeeCode";
    protected static final  String SHIFT_DATE = "shiftDate";
    protected static final String UPDATE_REQUEST = "updateRequest";
    protected static final String SHIFT_UPDATE_DATE = "shiftUpdateDate";

    public ShiftScheduleDao(boolean lcl) {
        this.local = lcl;
    }

    public ShiftScheduleDao() {
        this(true);
    }

    public void update(@NotNull ShiftSchedule schedule, boolean accepted) throws ShiftScheduleDaoException{
        JSONObject object = new JSONObject();
        object.put(SHIFT, schedule.getShiftCode());
        object.put(EMPLOYEE_CODE, schedule.getEmployeeCode());
        object.put(SHIFT_DATE, schedule.getShiftDate());
        object.put(UPDATE_REQUEST, schedule.isUpdateRequest());

        if (schedule.getShiftUpdateDate() != null){
            object.put(SHIFT_UPDATE_DATE, schedule.getShiftUpdateDate());
        }

        if (local){
            ShiftScheduleDaoFs.update(object, accepted);
        }else {
            ShiftScheduleDaoDb.update(object, accepted);
        }
    }

    public void pushShiftSchedule(@NotNull ShiftSchedule schedule) throws ShiftScheduleDaoException {
        JSONObject object = new JSONObject();
        object.put(SHIFT, schedule.getShiftCode());
        object.put(EMPLOYEE_CODE, schedule.getEmployeeCode());
        object.put(SHIFT_DATE, schedule.getShiftDate());
        object.put(UPDATE_REQUEST, schedule.isUpdateRequest());
        if (local) {
            ShiftScheduleDaoFs.pushSchedule(object);
        }else {
            ShiftScheduleDaoDb.pushSchedule(object);
        }
    }

    public List<ShiftSchedule> pullShiftSchedules(){
        List<ShiftSchedule> shiftScheduleList = new ArrayList<>();
        JSONArray array;
        if (local){
            array = ShiftScheduleDaoFs.pullShiftSchedules();
        }else {
            array = ShiftScheduleDaoDb.pullShiftSchedules();
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String shift = object.getString(SHIFT);
            String employeeCode = object.getString(EMPLOYEE_CODE);
            String shiftDate = object.getString(SHIFT_DATE);
            boolean updateRequest = object.getBoolean(UPDATE_REQUEST);
            ShiftSchedule schedule = fillSchedule(shift, employeeCode, shiftDate, updateRequest);
            if (updateRequest && schedule != null && (object.has(SHIFT_UPDATE_DATE))) {
                String shiftUpdateDate = object.getString(SHIFT_UPDATE_DATE);
                schedule.setShiftUpdateDate(shiftUpdateDate);
            }
            if (schedule != null) {
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

    public ShiftSchedule getShiftByKey(String sCode, String eCode, String dateTime) {
        ShiftSchedule schedule = null;
        JSONObject object;
        if (local){
             object = ShiftScheduleDaoFs.pullSchedule(sCode, eCode, dateTime);
        }else{
            object = ShiftScheduleDaoDb.pullSchedule(sCode, eCode, dateTime);
        }

        if (object != null){
            String shift = object.getString(SHIFT);
            String employeeCode = object.getString(EMPLOYEE_CODE);
            String shiftDate = object.getString(SHIFT_DATE);
            boolean updateRequest = object.getBoolean(UPDATE_REQUEST);
            schedule = this.fillSchedule(shift, employeeCode, shiftDate, updateRequest);
        }

        return schedule;
    }

    public void switchPersistence() {
        this.local = !local;
    }



    public boolean isLocal() {
        return this.local;
    }
}
