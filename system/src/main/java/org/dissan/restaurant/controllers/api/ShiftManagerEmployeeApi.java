package org.dissan.restaurant.controllers.api;

import org.dissan.restaurant.beans.ShiftScheduleBean;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;


/**
 * Interface that export ShiftManager operation for [EmployeeViews]
 */
public interface ShiftManagerEmployeeApi {
    /** void requestUpdate()
     * function that update a schedule request and store it in persistence layer.
     * @throws ShiftScheduleDaoException when there is some problem with schedule case updateRequest not true.
     */
    void requestUpdate() throws  ShiftScheduleDaoException;
    void getMySchedule();
    ShiftScheduleBean getBean();

    boolean switchPersistence();
}
