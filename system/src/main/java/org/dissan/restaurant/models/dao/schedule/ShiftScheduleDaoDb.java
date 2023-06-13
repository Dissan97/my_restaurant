package org.dissan.restaurant.models.dao.schedule;

import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.controllers.util.DBMS;
import org.dissan.restaurant.controllers.util.DBMSException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ShiftScheduleDaoDb {

    private ShiftScheduleDaoDb(){}
    @Contract(value = " -> new", pure = true)
    public static @NotNull JSONArray pullShiftSchedules() {
        JSONArray schedulesArray = new JSONArray();
        try(Connection connection = DBMS.open()){
            JSONObject schedule;
            String storedProcedure = "call pullSchedules()";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    schedule = new JSONObject();
                    schedule.put(ShiftScheduleDao.SHIFT, resultSet.getString(ShiftScheduleDao.SHIFT));
                    schedule.put(ShiftScheduleDao.EMPLOYEE_CODE, resultSet.getString("employee"));
                    schedule.put(ShiftScheduleDao.SHIFT_DATE, resultSet.getString(ShiftScheduleDao.SHIFT_DATE));
                    schedule.put(ShiftScheduleDao.UPDATE_REQUEST, resultSet.getBoolean(ShiftScheduleDao.UPDATE_REQUEST));
                    String updateRequest = resultSet.getString(ShiftScheduleDao.SHIFT);
                    if (updateRequest != null){
                        schedule.put(ShiftScheduleDao.SHIFT, updateRequest);
                    }schedulesArray.put(schedule);
                }
            }
            return schedulesArray;
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            Logger.getLogger(ShiftScheduleDaoDb.class.getSimpleName()).warning(e.getMessage());
            return schedulesArray;
        }
    }

    public static void update(@NotNull JSONObject schedule, boolean accepted) throws ShiftScheduleDaoException {
        String sCode = schedule.getString(ShiftScheduleDao.SHIFT);
        String eCode = schedule.getString(ShiftScheduleDao.EMPLOYEE_CODE);
        String date = schedule.getString(ShiftScheduleDao.SHIFT_DATE);
        String updateDate = "";
        if (schedule.has(ShiftScheduleDao.SHIFT_UPDATE_DATE)){
            updateDate = schedule.getString(ShiftScheduleDao.SHIFT_UPDATE_DATE);
        }
        try(Connection connection = DBMS.open()) {
            String storedProcedure = "call updateSchedule(?, ?, ?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, sCode);
                statement.setString(2, eCode);
                statement.setString(3, date);
                statement.setBoolean(4, accepted);
                statement.setString(5, updateDate);
                statement.execute();
            }
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            throw new ShiftScheduleDaoException(e.getMessage());
        }
    }
    //16/06/2023::10:30
    public static void pushSchedule(@NotNull JSONObject schedule) throws ShiftScheduleDaoException {
        String sCode = schedule.getString(ShiftScheduleDao.SHIFT);
        String eCode = schedule.getString(ShiftScheduleDao.EMPLOYEE_CODE);
        String date = schedule.getString(ShiftScheduleDao.SHIFT_DATE);
        try(Connection connection = DBMS.open()) {
            String storedProcedure = "call pushShiftSchedule(?, ? , ?)";

            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, sCode);
                statement.setString(2, eCode);
                statement.setString(3, date);
                statement.execute();
            }
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            throw new ShiftScheduleDaoException(e.getMessage());
        }
    }

    public static JSONObject pullSchedule(String sCode, String eCode, String dateTime) {
        try(Connection connection = DBMS.open()){
            JSONObject schedule;
            String storedProcedure = "call pullSchedule(?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, sCode);
                statement.setString(2, eCode);
                statement.setString(3, dateTime);
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    String shiftCode = resultSet.getString(ShiftScheduleDao.SHIFT);
                    String employeeCode = resultSet.getString(ShiftScheduleDao.EMPLOYEE_CODE);
                    String dateTimeDb = resultSet.getString(ShiftScheduleDao.SHIFT_DATE);
                    if (shiftCode.equals(sCode) && employeeCode.equals(eCode) && dateTimeDb.equals(dateTime)) {
                        schedule = new JSONObject();

                        schedule.put(ShiftScheduleDao.SHIFT, shiftCode);
                        schedule.put(ShiftScheduleDao.EMPLOYEE_CODE, employeeCode);
                        schedule.put(ShiftScheduleDao.SHIFT_DATE, dateTimeDb);
                        schedule.put(ShiftScheduleDao.UPDATE_REQUEST, resultSet.getBoolean(ShiftScheduleDao.UPDATE_REQUEST));
                        String updateRequest = resultSet.getString(ShiftScheduleDao.SHIFT);
                        if (updateRequest != null) {
                            schedule.put(ShiftScheduleDao.SHIFT, updateRequest);
                        }
                        return schedule;
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            Logger.getLogger(ShiftScheduleDaoDb.class.getSimpleName()).warning(e.getMessage());
            return null;
        }

        return null;
    }
}
