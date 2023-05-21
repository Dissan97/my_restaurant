package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.ConcreteUser;
import org.dissan.restaurant.models.UserRole;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDao {
    private boolean local = true;
    public static final Logger LOGGER = Logger.getLogger(UserDao.class.getSimpleName());
    public AbstractUser getUserByUsername(String username){
        JSONArray array;
        //Choosing to which persistence model get from my data
        if (this.isLocal()) {
            array = UserDaoFs.getUserByUserName(username);
        } else {
            array = UserDaoDb.getUserByUserName(username);
        }
        if (array == null){
            return null;
        }
        //Filling user data
        AbstractUser user = new ConcreteUser();
        user.setUsername(username);
        user.setPassword(array.getString(0));
        OutStream.println(array.getString(0));
        user.setName(array.getString(1));
        user.setSurname(array.getString(2));
        user.setCityOfBirth(array.getString(3));
        try {
            user.setDateOfBirth(array.getString(4));
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }
        UserRole role = UserRole.valueOf(array.getString(5));
        user.setRole(role);
        return user;
        }

    public void putUser(@NotNull AbstractUser userData) throws IOException, UserAlreadyExistException {


        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(userData.getPassword());
        array.put(userData.getName());
        array.put(userData.getSurname());
        array.put(userData.getCityOfBirth());
        array.put(userData.getDateOfBirth());
        array.put(userData.getRole().name());
        object.put(userData.getUsername(), array);

        if (this.isLocal()){
            UserDaoFs.putUser(object);
        }else {
            UserDaoDb.putUser(object);
        }
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public boolean isLocal() {
        return local;
    }


    public List<AbstractUser> pullUsers() {
        List<AbstractUser> employeeList = new ArrayList<>();
        JSONArray array;
        if (local){
            array = UserDaoFs.pullUsers();
        }else {
            array = null;
        }
        if (array != null){
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                AbstractUser user = new ConcreteUser();
                String key = object.keys().next();
                user.setUsername(key);
                user.setPassword(object.getJSONArray(key).getString(0));
                user.setName(object.getJSONArray(key).getString(1));
                user.setSurname(object.getJSONArray(key).getString(2));
                user.setCityOfBirth(object.getJSONArray(key).getString(3));
                try {
                    user.setDateOfBirth(object.getJSONArray(key).getString(4));
                } catch (ParseException e) {
                    LOGGER.info(e.getMessage());
                }
                UserRole role = UserRole.valueOf(object.getJSONArray(key).getString(5));
                user.setRole(role);
                employeeList.add(user);
            }
        }
        return employeeList;
    }

}
