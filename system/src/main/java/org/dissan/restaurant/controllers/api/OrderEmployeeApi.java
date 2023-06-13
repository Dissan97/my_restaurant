package org.dissan.restaurant.controllers.api;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.dissan.restaurant.patterns.structural.facade.TableDaoException;

public interface OrderEmployeeApi {
    TableObserver getObserver();
    TableBean getTableBean() throws TableDaoException;
}
