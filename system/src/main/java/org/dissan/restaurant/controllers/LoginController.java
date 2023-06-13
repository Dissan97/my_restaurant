package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.EmployeeBean;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;
import org.dissan.restaurant.controllers.exceptions.UserNotFoundException;
import org.dissan.restaurant.controllers.util.DBMS;
import org.dissan.restaurant.controllers.util.DBMSException;
import org.dissan.restaurant.controllers.util.HashUtil;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.ConcreteUser;
import org.dissan.restaurant.models.Employee;
import org.dissan.restaurant.models.dao.user.EmployeeDao;
import org.dissan.restaurant.models.dao.user.UserDao;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class LoginController {
    private UserBean userBean;
    private AbstractUser user;
    private UserDao dao;

    public LoginController() {
        this.userBean = new UserBean();
        this.setDao(new UserDao());
    }
    public void singIn() throws UserCredentialException, BadCommandEntryException {
        String username = this.userBean.getEntry(UserBeanCommand.USERNAME);
        String password = this.userBean.getEntry(UserBeanCommand.PASSWORD);
        checkUserAndPassword(username, password);
        this.userBean.setUser(this.user);
        try {
            DBMS.setActualRole(this.userBean.getRole().name());
        } catch (DBMSException e) {
            Logger.getLogger(this.getClass().getSimpleName()).warning(e.getMessage());
        }
    }

    private void checkUser(String username) throws UserNotFoundException {
       boolean local = true; // for future implementation
        this.dao.setLocal(local);
        AbstractUser userInfo = this.dao.getUserByUsername(username);
        if (userInfo == null){
            throw new UserNotFoundException(username + " does not exist");
        }
        this.user = userInfo;
    }

    private void checkUserAndPassword(String username, String password) throws UserCredentialException {
        checkUser(username);
        if (!this.user.getPassword().equals(HashUtil.hashString(password))){
            throw new UserCredentialException("Password miss match");
        }
    }
    /**
     * In this method the normal flow wants that the user exists it can throw user exist
     * if the user does not exist then it will create a new user and then store it
     * @throws BadCommandEntryException this is thrown if the entry checked is invalid
     * @throws UserAlreadyExistException this is thrown when the user exists
     */
    public void singUp() throws BadCommandEntryException, UserAlreadyExistException, IOException {
        String username = this.userBean.getEntry(UserBeanCommand.USERNAME);
        String password = HashUtil.hashString(this.userBean.getEntry(UserBeanCommand.PASSWORD));
        String name = this.userBean.getEntry(UserBeanCommand.NAME);
        String surname = this.userBean.getEntry(UserBeanCommand.SURNAME);
        String cityOfBirth = this.userBean.getEntry(UserBeanCommand.CITY_OF_BIRH);
        String dateOfBirth = this.userBean.getEntry(UserBeanCommand.DATE);
        String role = this.userBean.getEntry(UserBeanCommand.ROLE);
        try{
            checkUser(username);
            throw new UserAlreadyExistException(username + " already taken");
        }catch (UserNotFoundException e){
            user = new ConcreteUser(username, password, name, surname, cityOfBirth, dateOfBirth, role);
            this.dao.putUser(user);
        }

    }

    public UserBean getUserBean() {
        return userBean;
    }

    public UserDao getDao() {
        return dao;
    }

    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    public EmployeeBean getEmployeeBean(UserBean userBean) {
        List<Employee> employeeList = EmployeeDao.pullEmployees();
        EmployeeBean employeeBean = null;
        if (employeeList != null) {
            for (Employee e :
                    employeeList) {
                if (userBean.getUsername().equals(e.getUser().getUsername())) {
                    employeeBean = new EmployeeBean(e);
                }
            }
        }
        return employeeBean;
    }

    public void setUserBean(UserBean ub) {
        this.userBean = ub;
    }

    public void switchDao(){
        this.dao.switchDao();
    }
}
