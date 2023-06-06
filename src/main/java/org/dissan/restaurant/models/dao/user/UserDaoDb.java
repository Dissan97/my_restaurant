package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.util.DBMS;
import org.dissan.restaurant.controllers.util.DBMSException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoDb {
    @Contract(pure = true)
    public static @Nullable JSONObject getUserByUsername(String usr)  {
        try(Connection connection = DBMS.open()){
            JSONObject object = null;
            String storedProcedure = "call pullUserByUsername(?)";
            CallableStatement statement = connection.prepareCall(storedProcedure);
            statement.setString(1, usr);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                object = new JSONObject();
                object.put(UserDao.USERNAME, resultSet.getString(UserDao.USERNAME));
                object.put(UserDao.PASSWORD, resultSet.getString(UserDao.PASSWORD));
                object.put(UserDao.NAME, resultSet.getString(UserDao.NAME));
                object.put(UserDao.SURNAME, resultSet.getString(UserDao.SURNAME));
                object.put(UserDao.CITY_OF_BIRTH, resultSet.getString(UserDao.CITY_OF_BIRTH));
                object.put(UserDao.DATE_OF_BIRTH, resultSet.getString(UserDao.DATE_OF_BIRTH));
                object.put(UserDao.ROLE, resultSet.getString(UserDao.ROLE));
            }
            return object;
        } catch (SQLException | ClassNotFoundException | DBMSException e) {
            return null;
        }
    }

    public static void main(String[] args) throws DBMSException, SQLException, ClassNotFoundException {
        DBMS.setActualRole("LOGIN");
        OutStream.println(UserDaoDb.getUserByUsername("manager").toString());
    }

    public static void putUser(JSONObject object) {
        //to implement
    }
}
