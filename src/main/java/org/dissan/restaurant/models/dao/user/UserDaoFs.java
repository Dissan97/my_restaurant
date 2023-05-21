package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.cli.utils.OutStream;
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
    private static final Map<String, JSONArray> LOCAL_CACHE = new HashMap<>();
    private static final String USERS = "users.json";

    @Contract(pure = true)
    public static @Nullable JSONArray getUserByUserName(String username)  {
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
            OutStream.println(e.getMessage());
        }
    }

    public static void loadUsers() throws IOException{
        JSONArray userArray;
        try (InputStream reader = Objects.requireNonNull(UserDaoFs.class.getResourceAsStream(UserDaoFs.USERS))) {
            JSONTokener jsonTokener = new JSONTokener(reader);
            userArray = new JSONArray(jsonTokener);
            JSONObject object = userArray.getJSONObject(0);
            for (String k:
                    object.keySet()) {
                UserDaoFs.LOCAL_CACHE.putIfAbsent(k, object.getJSONArray(k));
            }
        }catch (NullPointerException e){
            UserDaoFs.initFile();
            loadUsers();
        }catch (JSONException e){
            OutStream.println(e.getMessage());
        }
    }

    public static void putUser(@NotNull JSONObject object) throws IOException, UserAlreadyExistException {

        String usr = object.keys().next();
        if (LOCAL_CACHE.containsKey(usr)){
            throw new UserAlreadyExistException(usr + " already exist");
        }else{
            loadUsers();
        }

        BufferedWriter writer = new BufferedWriter(
                new FileWriter(Objects.requireNonNull(UserDaoFs.class.getResource(UserDaoFs.USERS)).getPath())
        );
        JSONArray array = new JSONArray();
        for (String k:
             UserDaoFs.LOCAL_CACHE.keySet()) {
            array.put(UserDaoFs.LOCAL_CACHE.get(k));
        }
        array.write(writer);
        writer.close();
        UserDaoFs.LOCAL_CACHE.put(usr, array);
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
        for (Map.Entry<String, JSONArray> entry:
                LOCAL_CACHE.entrySet()) {
            JSONObject object = new JSONObject();
            object.put(entry.getKey(), entry.getValue());
            array.put(object);
        }
        return array;
    }

}


