package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.controllers.util.DBMS;
import org.dissan.restaurant.controllers.util.DBMSException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoDb {

    private UserDaoDb(){}

    @Contract(pure = true)
    public static @Nullable JSONObject getUserByUsername(String usr)  {
        try(Connection connection = DBMS.open()){
            JSONObject object = null;
            String storedProcedure = "call pullUserByUsername(?)";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, usr);
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    object = new JSONObject();
                    object.put(UserDao.USERNAME, resultSet.getString(UserDao.USERNAME));
                    object.put(UserDao.PASSWORD, resultSet.getString(UserDao.PASSWORD));
                    object.put(UserDao.NAME, resultSet.getString(UserDao.NAME));
                    object.put(UserDao.SURNAME, resultSet.getString(UserDao.SURNAME));
                    object.put(UserDao.CITY_OF_BIRTH, resultSet.getString(UserDao.CITY_OF_BIRTH));
                    object.put(UserDao.DATE_OF_BIRTH, resultSet.getString(UserDao.DATE_OF_BIRTH));
                    object.put(UserDao.ROLE, resultSet.getString(UserDao.ROLE));
                }
            }
            return object;
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            return null;
        }

    }


    public static void putUser(@NotNull JSONObject object) throws IOException {
        try(Connection connection = DBMS.open()){
            String storedProcedure = "call pushUser(?, ? , ?, ?, ?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
                statement.setString(1, object.getString(UserDao.USERNAME));
                statement.setString(2, object.getString(UserDao.PASSWORD));
                statement.setString(3, object.getString(UserDao.NAME));
                statement.setString(4, object.getString(UserDao.SURNAME));
                statement.setString(5, object.getString(UserDao.DATE_OF_BIRTH));
                statement.setString(6, object.getString(UserDao.CITY_OF_BIRTH));
                statement.setString(7, object.getString(UserDao.ROLE));
                statement.execute();
            }
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            throw new IOException();
        }
    }
}
