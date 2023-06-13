package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.ConcreteUser;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private boolean local = true;
    protected static final String USERNAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String NAME = "name";
    protected static final String SURNAME = "surname";
    protected static final String CITY_OF_BIRTH = "cityOfBirth";
    protected static final String DATE_OF_BIRTH = "dateOfBirth";
    protected static final String ROLE = "role";
    public AbstractUser getUserByUsername(String uName){
        JSONObject object;
        //Choosing to which persistence model get from my data
        if (this.isLocal()) {
            object = UserDaoFs.getUserByUserName(uName);
        } else {
            object = UserDaoDb.getUserByUsername(uName);
        }
        if (object == null){
            return null;
        }
        //Filling user data
        return buildUser(object);
    }

    private @NotNull AbstractUser buildUser(@NotNull JSONObject object){
        String usr = object.getString(USERNAME);
        String pwd = object.getString(PASSWORD);
        String nm = object.getString(NAME);
        String sn = object.getString(SURNAME);
        String city = object.getString(CITY_OF_BIRTH);
        String date = object.getString(DATE_OF_BIRTH);
        String rl = object.getString(ROLE);
        return new ConcreteUser(usr, pwd, nm, sn, city, date, rl);
    }

    public void putUser(@NotNull AbstractUser userData) throws IOException, UserAlreadyExistException {
        JSONObject object = new JSONObject();
        object.put(USERNAME,userData.getUsername());
        object.put(PASSWORD,userData.getPassword());
        object.put(NAME,userData.getName());
        object.put(SURNAME,userData.getSurname());
        object.put(CITY_OF_BIRTH,userData.getCityOfBirth());
        object.put(DATE_OF_BIRTH,userData.getDateOfBirth());
        object.put(ROLE,userData.getRole().name());
        if (this.isLocal()){
            UserDaoFs.putUser(object);
        }else {
            UserDaoDb.putUser(object);
        }
    }

    public List<AbstractUser> pullUsers() {
        List<AbstractUser> userList = new ArrayList<>();
        JSONArray array;
        if (local){
            array = UserDaoFs.pullUsers();
        }else {
            array = null;
        }
        if (array != null){
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                userList.add(buildUser(object));
            }
        }
        return userList;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public boolean isLocal() {
        return local;
    }

    public void switchDao() {
        this.setLocal(!isLocal());
    }
}
