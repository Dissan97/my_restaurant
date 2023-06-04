package org.dissan.restaurant.controllers.api;

import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.ShiftScheduleBean;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;


/**
 * Interface that export ShiftManager operation for [EmployeeViews]
 */
public interface ShiftManagerEmployeeApi {
    void requestUpdate() throws EmployeeDaoException, ShiftDaoException, ShiftScheduleDaoException, ShiftDateException;
    void getMySchedule();
    ShiftScheduleBean getBean();
}
