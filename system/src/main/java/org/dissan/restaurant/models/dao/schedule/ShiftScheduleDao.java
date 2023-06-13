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
import java.util.logging.Logger;

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
        JSONObject object = null;
        if (local){
             object = ShiftScheduleDaoFs.pullSchedule(sCode, eCode, dateTime);
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
        Logger logger = Logger.getLogger(this.getClass().getSimpleName());
        try {
            checkConsistency();
        } catch (ShiftScheduleDaoException e) {
            logger.warning(e.getMessage());
        }

    }

    private void checkConsistency() throws ShiftScheduleDaoException {
        JSONArray fsSchedules = ShiftScheduleDaoFs.pullShiftSchedules();
        JSONArray dbSchedules = ShiftScheduleDaoDb.pullShiftSchedules();

        for (int i = 0; i < fsSchedules.length(); i++) {
            JSONObject scheduleFs = fsSchedules.getJSONObject(i);
            checkDbDifferenceFromFs(dbSchedules, scheduleFs);
        }

        for (int i = 0; i < dbSchedules.length(); i++) {
            JSONObject scheduleDb = dbSchedules.getJSONObject(i);
            checkFsDifferenceFromDb(fsSchedules, scheduleDb);
        }



    }

    private void checkDbDifferenceFromFs(JSONArray dbSchedules, JSONObject scheduleFs) throws ShiftScheduleDaoException {
        String shiftFs = scheduleFs.getString(SHIFT);
        String employeeCodeFs = scheduleFs.getString(EMPLOYEE_CODE);
        String shiftDateFs = scheduleFs.getString(SHIFT_DATE);
        for (int j = 0; j < dbSchedules.length(); j++) {
            JSONObject scheduleDb = dbSchedules.getJSONObject(j);
            String shiftDb = scheduleDb.getString(SHIFT);
            String employeeCodeDb = scheduleDb.getString(EMPLOYEE_CODE);
            String shiftDateDb = scheduleDb.getString(SHIFT_DATE);
            if (!(shiftFs.equals(shiftDb) || (employeeCodeFs.equals(employeeCodeDb) || shiftDateFs.equals(shiftDateDb)))){
                ShiftScheduleDaoFs.pushSchedule(scheduleFs);
                break;
            }
        }
    }

    private void checkFsDifferenceFromDb(JSONArray fsSchedules, JSONObject scheduleDb) throws ShiftScheduleDaoException {
        String shiftFs = scheduleDb.getString(SHIFT);
        String employeeCodeFs = scheduleDb.getString(EMPLOYEE_CODE);
        String shiftDateFs = scheduleDb.getString(SHIFT_DATE);
        for (int j = 0; j < fsSchedules.length(); j++) {
            JSONObject scheduleFs = fsSchedules.getJSONObject(j);
            String shiftDb = scheduleFs.getString(SHIFT);
            String employeeCodeDb = scheduleFs.getString(EMPLOYEE_CODE);
            String shiftDateDb = scheduleFs.getString(SHIFT_DATE);
            if (!(shiftFs.equals(shiftDb) || (employeeCodeFs.equals(employeeCodeDb) || shiftDateFs.equals(shiftDateDb)))){
                ShiftScheduleDaoDb.pushSchedule(scheduleDb);
                break;
            }
        }
    }

}
