package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.controllers.exceptions.UserAlreadyExistException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UserDaoFs {

    private UserDaoFs() {}
    private static final Map<String, JSONObject> LOCAL_CACHE = new HashMap<>();
    private static final String USERS = "users.json";
    private static final String USERNAME = "username";

    private static final  Logger LOGGER = Logger.getLogger(UserDaoFs.class.getSimpleName());

    @Contract(pure = true)
    public static @Nullable JSONObject getUserByUserName(String username)  {
        if (!LOCAL_CACHE.containsKey(username)){
            try {
                UserDaoFs.loadUsers();
            } catch (IOException e) {
                return null;
            }
        }
        return UserDaoFs.LOCAL_CACHE.get(username);
    }

    private static void initFile() {
        String rawPath = Objects.requireNonNull(UserDaoFs.class.getResource("")).getPath();
        StringBuilder builder = new StringBuilder(rawPath);
        builder.append('/').append(UserDaoFs.USERS);
        File file = new File(builder.toString());
        try {
            if (file.createNewFile()){
                Logger logger = Logger.getLogger(UserDaoFs.class.getSimpleName());
                builder.append(": File created");
                String message = builder.toString();
                logger.info(message);
            }
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static void loadUsers() throws IOException{
        JSONArray userArray;
        try (InputStream reader = Objects.requireNonNull(UserDaoFs.class.getResourceAsStream(UserDaoFs.USERS))) {
            JSONTokener jsonTokener = new JSONTokener(reader);
            userArray = new JSONArray(jsonTokener);
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject object = userArray.getJSONObject(i);
                String usr = object.getString(USERNAME);
                LOCAL_CACHE.put(usr, object);
            }

        }catch (NullPointerException e){
            UserDaoFs.initFile();
            loadUsers();
        }catch (JSONException e){
            LOGGER.warning(e.getMessage());
        }
    }

    public static void putUser(@NotNull JSONObject object) throws IOException, UserAlreadyExistException {

        String usr = object.getString(USERNAME);
        if (LOCAL_CACHE.containsKey(usr)){
            throw new UserAlreadyExistException(usr + " already exist");
        }else{
            loadUsers();
        }

        try(BufferedWriter writer = new BufferedWriter(
                new FileWriter(Objects.requireNonNull(UserDaoFs.class.getResource(UserDaoFs.USERS)).getPath())
        )) {
            JSONArray array = new JSONArray();
            for (Map.Entry<String, JSONObject> entry :
                    LOCAL_CACHE.entrySet()) {
                array.put(entry.getValue());
            }
            array.write(writer);
        }catch (IOException e){
            throw new IOException();
        }
        UserDaoFs.LOCAL_CACHE.put(usr, object);
    }

    public static @Nullable JSONArray pullUsers() {
        if (LOCAL_CACHE.isEmpty()){
            try {
                loadUsers();
            } catch (IOException e) {
                return null;
            }
        }
        JSONArray array = new JSONArray();
        for (Map.Entry<String, JSONObject> entry:
                LOCAL_CACHE.entrySet()) {
            array.put(entry.getValue());
        }
        return array;
    }

}


