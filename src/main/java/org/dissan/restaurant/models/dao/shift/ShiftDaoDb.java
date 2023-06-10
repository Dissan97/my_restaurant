package org.dissan.restaurant.models.dao.shift;


import org.dissan.restaurant.cli.utils.OutStream;
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

public class ShiftDaoDb {

    /**
     * Method call to initialize database connection metadata needed to access DataBase
     */

    private ShiftDaoDb(){}
    @Contract(pure = true)
    public static @NotNull JSONArray getShiftList() throws SQLException, ClassNotFoundException, DBMSException {
        JSONArray array;
        try(Connection connection = DBMS.open()){
            String storedProcedure = "call pullShifts()";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)){
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                array = new JSONArray();
                while (resultSet.next()){
                    JSONObject object = new JSONObject();
                    object.put(ShiftDao.CODE, resultSet.getString(ShiftDao.CODE));
                    object.put(ShiftDao.TASK, resultSet.getString(ShiftDao.TASK));
                    object.put(ShiftDao.ROLE, resultSet.getString(ShiftDao.ROLE));
                    object.put(ShiftDao.SALARY, resultSet.getDouble(ShiftDao.SALARY));
                    array.put(object);
                }
                resultSet.close();
            }
        }
        OutStream.print(array.toString());
        return array;
    }
}
