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
                    schedule.put(ShiftScheduleDao.EMPLOYEE_CODE, resultSet.getString(ShiftScheduleDao.EMPLOYEE_CODE));
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
            return schedulesArray;
        }
    }

    public static void update(@NotNull JSONObject schedule, boolean accepted) throws ShiftScheduleDaoException {
        String sCode = schedule.getString(ShiftScheduleDao.SHIFT);
        String eCode = schedule.getString(ShiftScheduleDao.EMPLOYEE_CODE);
        String date = schedule.getString(ShiftScheduleDao.SHIFT_DATE);
        try(Connection connection = DBMS.open()) {
            String storedProcedure = "call updateShift(?, ? , ?, ?)";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, sCode);
                statement.setString(2, eCode);
                statement.setString(3, date);
                statement.setBoolean(4, accepted);
                statement.execute();
            }
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            throw new ShiftScheduleDaoException(e.getMessage());
        }
    }

    public static void pushSchedule(@NotNull JSONObject schedule) throws ShiftScheduleDaoException {
        String sCode = schedule.getString(ShiftScheduleDao.SHIFT);
        String eCode = schedule.getString(ShiftScheduleDao.EMPLOYEE_CODE);
        String date = schedule.getString(ShiftScheduleDao.SHIFT_DATE);
        try(Connection connection = DBMS.open()) {
            String storedProcedure = "call pushShift(?, ? , ?)";
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
}
