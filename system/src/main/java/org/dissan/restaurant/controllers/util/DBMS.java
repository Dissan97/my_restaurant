package org.dissan.restaurant.controllers.util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DBMS {

    private static final String CONF = "conf.json";
    private static Map<String, JSONObject> localCache;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String  ADDRESS = "url";
    private static boolean firstConnection = true;
    private static String actualRole;
    private static String url;

    private DBMS() {
    }


    public static Connection open() throws SQLException, ClassNotFoundException, DBMSException {
        if (firstConnection){
            firstConnection = false;
            Class.forName("org.mariadb.jdbc.Driver");
        }
        loadLocalCache();
        String usr = localCache.get(actualRole).getString(USERNAME);
        String pwd = localCache.get(actualRole).getString(PASSWORD);
        return DriverManager.getConnection(url, usr, pwd);
    }

    public static void setActualRole(String role) throws DBMSException {
        loadLocalCache();
        if (!localCache.containsKey(role)){
            throw new DBMSException("this role: " + role + " does not exist");
        }
        actualRole = role;
    }

    private static void loadLocalCache() throws DBMSException {
        if (localCache == null) {
            try (InputStream inputStream = Objects.requireNonNull(DBMS.class.getResourceAsStream(CONF))) {
                JSONTokener jsonTokener = new JSONTokener(inputStream);
                JSONObject configurations = new JSONObject(jsonTokener);
                localCache = new HashMap<>();
                for (String conf :
                        configurations.keySet()) {
                    if (!conf.equals(ADDRESS)) {
                        localCache.put(conf, configurations.getJSONObject(conf));
                    }
                }
                url = configurations.getString(ADDRESS);
            } catch (IOException e) {
                throw new DBMSException("io problem: " + e.getMessage());
            }
        }
    }


}
