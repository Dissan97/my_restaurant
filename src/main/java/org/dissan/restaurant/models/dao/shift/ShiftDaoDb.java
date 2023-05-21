package org.dissan.restaurant.models.dao.shift;


import org.dissan.restaurant.models.dao.ConfigurationDBMS;
import org.dissan.restaurant.models.dao.DaoActor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ShiftDaoDb {

    /**
     * Method call to initialize database connection metadata needed to access DataBase
     */


    public static void init() {
        //Need change to avoid over-coupling
        conn = ConfigurationDBMS.loadConf(DaoActor.MANAGER);
    }

    static Connection conn = null;




    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/restaurant?user=root&password=pass");


        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    @Contract(pure = true)
    public static @Nullable JSONObject getShiftList() {
        return null;
    }
}
