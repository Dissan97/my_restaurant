package org.dissan.restaurant.controllers;

import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.beans.ShiftScheduleBean;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.models.*;
import org.dissan.restaurant.models.dao.shedule.ShiftScheduleDao;
import org.dissan.restaurant.models.dao.shift.ShiftDao;
import org.dissan.restaurant.models.dao.user.UserDao;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Controller ->
 *     information expert {Employee, Shift, ShiftSchedule, EmployeeDao, ShiftDao, ShiftScheduleDao}
 */

public class ShiftManager implements ShiftManagerEmployeeApi {

    private final ShiftScheduleBean bean;
    private final ShiftSchedule shiftSchedule;
    private final ShiftScheduleDao shiftScheduleDao;

    private List<Shift> shiftList = new ArrayList<>();
    private List<AbstractUser> employeeList = new ArrayList<>();

    public ShiftManager() {

        //Preparing the stuff
        Shift shift = new Shift();
        AbstractUser user = new ConcreteUser();
        this.shiftSchedule = new ShiftSchedule(shift, user);
        this.bean = new ShiftScheduleBean(this.shiftSchedule);
        this.shiftScheduleDao = new ShiftScheduleDao();
        fillEmployees();
        fillShifts();

    }


    private void fillEmployees() {
        ShiftDao dao = new ShiftDao();
        this.shiftList = dao.getShiftList();
    }
    private void fillShifts() {
        UserDao dao = new UserDao();
        this.employeeList = dao.pullUsers();
        this.employeeList.removeIf(usr -> usr.getRole() != UserRole.ATTENDANT || usr.getRole() != UserRole.COOKER);
    }

    /**
     *
     * @param sCode shift code used to check shift
     * @param eCode employee code used to check employee
     * @param dateTime dateTime is passed from user to create new shiftSchedule
     * @throws EmployeeDaoException When an employee does not exist
     * @throws ShiftDaoException When a shift does not exist
     * @throws ShiftDateException When is passed a bad date for the context
     * @throws ShiftScheduleDaoException when is passed a bad schedule
     */
    public void assignShift(String sCode, String eCode, String dateTime) throws EmployeeDaoException, ShiftDaoException, ShiftDateException, ShiftScheduleDaoException {
        controlEmployee(eCode);
        controlShift(sCode);
        this.shiftSchedule.setDateTime(dateTime);

        ShiftScheduleDao dao = new ShiftScheduleDao();
        try {
            ShiftSchedule vShiftSchedule = dao.getShiftByKey(sCode, eCode, dateTime);
            String key = this.shiftSchedule.toString();
            String keyToVerify = vShiftSchedule.toString();
            if (key.equals(keyToVerify)){
                throw new ShiftScheduleDaoException();
            }
        }catch (NullPointerException ignored){
            //This block is ignored
        }finally {
            dao.pushShiftSchedule(this.shiftSchedule);
        }
    }

    public void requestUpdate(String sCode, String eCode, String dateTime) throws EmployeeDaoException, ShiftDaoException, ShiftScheduleDaoException, ShiftDateException {
        controlEmployee(eCode);
        controlShift(sCode);
	    ShiftSchedule schedule = this.getRelativeShiftSchedule(sCode, eCode, dateTime);
	    schedule.setDateTime(dateTime);
	    shiftScheduleDao.update(schedule);
    }

    @Override
    public void getEmpSchedule(String eCode) {
        //to implement
    }

    public void acceptRequest(String sCode, String eCode, String dt){
        //to implement
    }

    public void getUpdateRequest(){

	    List<ShiftSchedule> shiftScheduleList;
	    shiftScheduleList = shiftScheduleDao.getShiftUpdateRequest();
	    if (!shiftScheduleList.isEmpty()){
	    	this.bean.setUpdateRequestList(shiftScheduleList);
	    }
    }

    private @NotNull ShiftSchedule getRelativeShiftSchedule(String sCode, String eCode, String dateTime) throws ShiftScheduleDaoException {
        ShiftSchedule schedule = null;
        schedule = this.shiftScheduleDao.getShiftByKey(sCode, eCode, dateTime);
        if (schedule == null){
            throw new ShiftScheduleDaoException();
        }
        return schedule;
    }

    private void controlShift(String sCode) throws ShiftDaoException {
        for (Shift sft:
             this.shiftList) {
            if (sCode.equals(sft.getCode())){
                return;
            }
        }
        throw new ShiftDaoException(sCode);
    }

    private void controlEmployee(String eCode) throws EmployeeDaoException {
        for (AbstractUser usr:
             this.employeeList) {
            if (usr.getUsername().equals(eCode)){
                return;
            }
        }
        throw new EmployeeDaoException(eCode);
    }

    public void pullSchedules(){
        if (controlSchedules()) {
            List<ShiftSchedule> schedules = this.shiftScheduleDao.pullShiftSchedules();
            this.bean.setShiftScheduleList(schedules);
        }
    }

    private boolean controlSchedules() {
        List<ShiftSchedule> schedules = this.bean.getShiftScheduleList();
        if (schedules == null){
            return false;
        }

        if (schedules.isEmpty()){
            return false;
        }

        for (ShiftSchedule s:
             schedules) {
            if (!ModelUtil.goodDate(s.getDateTime())){
                return false;
            }
        }

        return true;
    }

    public ShiftScheduleBean getBean() {
        return this.bean;
    }


}
