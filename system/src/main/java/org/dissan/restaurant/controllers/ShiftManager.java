package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.*;
import org.dissan.restaurant.controllers.api.ShiftManagerEmployeeApi;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDaoException;
import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.dissan.restaurant.models.Employee;
import org.dissan.restaurant.models.Shift;
import org.dissan.restaurant.models.ShiftSchedule;
import org.dissan.restaurant.models.dao.schedule.ShiftScheduleDao;
import org.dissan.restaurant.models.dao.shift.ShiftDao;
import org.dissan.restaurant.models.dao.user.EmployeeDao;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Controller -> (Use cases : {Assign Shift, )
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
     * use case assign shift to an employee data are collected from the ShiftBean, they are parsed
     * then stored in persistence.
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
        try {
            ShiftSchedule vShiftSchedule = this.shiftScheduleDao.getShiftByKey(sCode, eCode, dateTime);
            String key = shiftSchedule.toString();
            String keyToVerify = vShiftSchedule.toString();
            if (key.equals(keyToVerify)){
                throw new ShiftScheduleDaoException("shift already exist");
            }
        }catch (NullPointerException ignored){
            //This block is ignored
        }
        this.shiftScheduleDao.pushShiftSchedule(shiftSchedule);
    }


    @Override
    public void requestUpdate() throws ShiftScheduleDaoException {
        ShiftSchedule schedule = this.bean.getShiftSchedule();
        schedule.setShiftUpdateDate(this.bean.getRelativeEntry(ShiftBeanCommand.UPDATE_DATE_TIME));
	    shiftScheduleDao.update(schedule, false);
    }

    /**
     * function that collect data from database and update schedules foreach employee
     */
    @Override
    public void getMySchedule() {
        //to implement
        ShiftScheduleDao dao = new ShiftScheduleDao();
        List<ShiftSchedule> shiftScheduleList = dao.pullShiftSchedules();

        shiftScheduleList.removeIf(
                shiftSchedule ->
                {
                    //remove not employee schedule
                    boolean employeeCode = !shiftSchedule.getEmployeeCode().equals(this.bean.getEmployeeBean().getCode());
                    //remove bad shift date -> before this day or day before the method call
                    boolean employeeDate = BeanUtil.goodDate(shiftSchedule.getShiftDate(), true) == null && !shiftSchedule.isUpdateRequest();
                    return employeeCode || employeeDate;
                }
        );

        this.bean.setShiftScheduleList(shiftScheduleList);
    }

    
    public void manageRequest(boolean accepted) throws ShiftScheduleDaoException {
        //to implement
        ShiftSchedule schedule = this.bean.getShiftSchedule();
        this.shiftScheduleDao.update(schedule, accepted);
    }

    /**
     * getting update request shift from shift list and then updating the relative bean
     */

    public void getUpdateRequest(){
	    List<ShiftSchedule> shiftScheduleList = shiftScheduleDao.pullShiftSchedules();
        //removing schedule that does not have update request or has updated but does not have updateDate.
        shiftScheduleList.removeIf(schedule ->
                 BeanUtil.goodDate(schedule.getShiftDate(), true) == null ||
                        !(schedule.isUpdateRequest() && schedule.getShiftUpdateDate() != null )
        );
        this.bean.setUpdateRequestList(shiftScheduleList);
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
        List<ShiftSchedule> scheduleList = this.shiftScheduleDao.pullShiftSchedules();
        scheduleList.removeIf(shiftSchedule -> BeanUtil.goodDate(shiftSchedule.getShiftDate(), true) == null);
        this.bean.setShiftScheduleList(scheduleList);
    }


    @Override
    public ShiftScheduleBean getBean() {
        return this.bean;
    }

    @Override
    public boolean switchPersistence() {
        this.shiftScheduleDao.switchPersistence();
        return shiftScheduleDao.isLocal();
    }

}
