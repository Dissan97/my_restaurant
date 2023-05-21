package org.dissan.restaurant.controllers;

public class DBMS {
    private static DBMS dbms = null;


    private DBMS() {
        //todo ad configuration to activate connection with database
    }

    public static void initDbms(){
        if (DBMS.dbms == null){
            DBMS.dbms = new DBMS();
        }
    }

    public static DBMS getInstance() {
        DBMS.initDbms();
        return DBMS.dbms;
    }



    public static void closeConnection(){
        DBMS.dbms.close();
    }

    private void close() {

    }
}
