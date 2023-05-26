package org.dissan.restaurant.controllers;

import org.dissan.restaurant.beans.BadCommanEntryException;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.controllers.exceptions.UserCredentialException;
import org.dissan.restaurant.controllers.exceptions.UserNotFoundException;
import org.dissan.restaurant.controllers.util.HashUtil;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.ConcreteUser;
import org.dissan.restaurant.models.dao.user.UserDao;
import java.io.IOException;

public class LoginController {
    private final UserBean userBean;
    private AbstractUser user;
    private UserDao dao;
    public LoginController() {
        this.userBean = new UserBean();
        this.setDao(new UserDao());
    }
    public void singIn() throws UserCredentialException, BadCommanEntryException {
        String username = this.userBean.getEntry(UserBeanCommand.USERNAME);
        String password = this.userBean.getEntry(UserBeanCommand.PASSWORD);
        checkUserAndPassword(username, password);
        this.userBean.setUser(this.user);
    }

    private void checkUser(String username) throws UserNotFoundException {
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

    public void singUp() throws BadCommanEntryException, UserAlreadyExistException, IOException {
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
}
