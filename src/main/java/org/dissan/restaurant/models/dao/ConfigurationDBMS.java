package org.dissan.restaurant.models.dao;

import java.sql.Connection;


public class ConfigurationDBMS {

    public static ConfigurationDBMS conf = null;
    private Connection conn;

    private ConfigurationDBMS(DaoActor actor) {

    }

    public static Connection loadConf(DaoActor actor) {
        ConfigurationDBMS conf = new ConfigurationDBMS(actor);
        return ConfigurationDBMS.conf.getConnection();
    }

    private Connection getConnection() {
        return this.conn;
    }
}
