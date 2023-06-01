package org.dissan.restaurant.controllers.api;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.structural.facade.TableDaoException;

import java.util.List;

public interface CustomerOrderApi {

    void sendOrder();
    void pay();
    TableBean getTableBean(String tableName, int clients) throws TableDaoException;
    List<String> getFreeTables();

    String printFreeTables();

}
