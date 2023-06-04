package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.*;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.models.*;
import org.dissan.restaurant.models.dao.schedule.ShiftScheduleDao;
import org.dissan.restaurant.models.dao.shift.ShiftDao;
import org.dissan.restaurant.models.dao.user.EmployeeDao;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Controller ->
 *     information expert {Employee, Shift, ShiftSchedule, EmployeeDao, ShiftDao, ShiftScheduleDao}
 */

public class ShiftManager implements ShiftManagerEmployeeApi {

    private final ShiftScheduleBean bean;
    private final ShiftScheduleDao shiftScheduleDao;

    private List<Shift> shiftList = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();

    public ShiftManager() {
        //Preparing the stuff
        this.bean = new ShiftScheduleBean();
        this.shiftScheduleDao = new ShiftScheduleDao();
        pullShifts();
        pullEmployees();
    }

    public ShiftManager(UserBean userBean) throws EmployeeDaoException {
        this.bean = new ShiftScheduleBean();
        pullEmployees();
        pullShifts();
        Employee employee = null;
        this.shiftScheduleDao = new ShiftScheduleDao();
        for (Employee emp:
             employeeList) {
            if (emp.getUser().getUsername().equals(userBean.getUsername())){
                employee = emp;
                break;
            }
        }
        if (employee == null){
            throw new EmployeeDaoException("some problem with employee");
        }
        EmployeeBean employeeBean = new EmployeeBean(employee);
        this.bean.setEmployeeBean(employeeBean);
    }

    private void pullShifts() {
        ShiftDao dao = new ShiftDao();
        this.shiftList = dao.getShiftList();
        this.bean.setShiftList(this.shiftList);
    }
    private void pullEmployees() {
        this.employeeList = EmployeeDao.pullEmployees();
    }

    /**
     *
     * @throws EmployeeDaoException When an employee does not exist
     * @throws ShiftDaoException When a shift does not exist
     * @throws ShiftDateException When is passed a bad date for the context
     * @throws ShiftScheduleDaoException when is passed a bad schedule
     */
    public void assignShift() throws EmployeeDaoException, ShiftDaoException, ShiftDateException, ShiftScheduleDaoException {
        String sCode = this.bean.getRelativeEntry(ShiftBeanCommand.SHIFT_CODE);
        String eCode = this.bean.getRelativeEntry(ShiftBeanCommand.EMPLOYEE_CODE);
        String dateTime = this.bean.getRelativeEntry(ShiftBeanCommand.DATE_TIME);
        Employee emp = controlEmployee(eCode);
        Shift sft = controlShift(sCode);
        ShiftSchedule shiftSchedule = new ShiftSchedule(sft, emp, dateTime);
        ShiftScheduleDao dao = new ShiftScheduleDao();
        try {
            ShiftSchedule vShiftSchedule = dao.getShiftByKey(sCode, eCode, dateTime);
            String key = shiftSchedule.toString();
            String keyToVerify = vShiftSchedule.toString();
            if (key.equals(keyToVerify)){
                throw new ShiftScheduleDaoException("shift already exist");
            }
        }catch (NullPointerException ignored){
            //This block is ignored
        }
        dao.pushShiftSchedule(shiftSchedule);
    }

    public void requestUpdate() throws EmployeeDaoException, ShiftDaoException, ShiftScheduleDaoException, ShiftDateException {
        String sCode = this.bean.getRelativeEntry(ShiftBeanCommand.SHIFT_CODE);
        String eCode = this.bean.getRelativeEntry(ShiftBeanCommand.EMPLOYEE_CODE);
        String dateTime = this.bean.getRelativeEntry(ShiftBeanCommand.DATE_TIME);
        controlEmployee(eCode);
        controlShift(sCode);
	    ShiftSchedule schedule = this.getRelativeShiftSchedule(sCode, eCode, dateTime);
	    schedule.setShiftDate(dateTime);
	    shiftScheduleDao.update(schedule);
    }

    @Override
    public void getMySchedule() {
        //to implement
        ShiftScheduleDao dao = new ShiftScheduleDao();
        List<ShiftSchedule> shiftScheduleList = dao.pullShiftSchedules();
        //todo updaet userBean in employeeBean
        shiftScheduleList.removeIf(
                shiftSchedule -> !shiftSchedule.getEmployeeCode().equals(this.bean.getUserBean()) && !shiftSchedule.isUpdateRequest()
        );

        this.bean.setShiftScheduleList(shiftScheduleList);
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
            throw new ShiftScheduleDaoException("shift already exist");
        }
        return schedule;
    }

    private @NotNull Shift controlShift(String sCode) throws ShiftDaoException {
        for (Shift sft:
             this.shiftList) {
            if (sCode.equals(sft.getCode())){
                return sft;
            }
        }
        throw new ShiftDaoException(sCode);
    }

    private @NotNull Employee controlEmployee(String eCode) throws EmployeeDaoException {
        for (Employee emp:
             this.employeeList) {
            if (emp.getEmployeeCode().equals(eCode)){
                return emp;
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
            if (BeanUtil.goodDate(s.getShiftDate(), true) == null){
                return false;
            }
        }
        return true;
    }

    @Override
    public ShiftScheduleBean getBean() {
        return this.bean;
    }




}
