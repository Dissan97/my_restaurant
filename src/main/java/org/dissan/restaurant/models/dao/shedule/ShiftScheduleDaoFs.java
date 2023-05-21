package org.dissan.restaurant.models.dao.shedule;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.models.ModelUtil;
import org.dissan.restaurant.models.dao.user.UserDaoFs;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ShiftScheduleDaoFs {

    private ShiftScheduleDaoFs(){}
    private static final String SCHEDULES = "schedules.json";

    private static final Map<String, JSONObject> LOCAL_CACHE = new HashMap<>();
    public static @Nullable JSONArray pullShiftSchedules() {

        checkLocalCache();

        if (LOCAL_CACHE.isEmpty()){
            return null;
        }

        Set<String> keys = LOCAL_CACHE.keySet();
        JSONArray array = new JSONArray();
        int i = 0;
        for (String k:
             keys) {
            array.put(i++, LOCAL_CACHE.get(k));
        }

        return array;
    }

    private static void checkLocalCache() {
        if (LOCAL_CACHE.isEmpty()){
            loadSchedules();
        }

        for (String s:
             LOCAL_CACHE.keySet()) {
            if (!ModelUtil.goodDate(s)){
                loadSchedules();
            }
        }

    }


    private static void loadSchedules(){
        JSONArray schedulesArray;
        LOCAL_CACHE.clear();
        try (InputStream reader = Objects.requireNonNull(UserDaoFs.class.getResourceAsStream(ShiftScheduleDaoFs.SCHEDULES))){
            JSONTokener jsonTokener = new JSONTokener(reader);
            schedulesArray = new JSONArray(jsonTokener);

            for (int i = 0; i < schedulesArray.length(); i++){
                JSONObject schedule = schedulesArray.getJSONObject(i);
                String key = schedule.keys().next();
                String[] superKey = key.split(";");

                if (ModelUtil.goodDate(superKey[2])){
                    LOCAL_CACHE.put(key, schedule);
                }

            }

        }catch (IOException | NullPointerException e){
            OutStream.println("UserDao{loadSchedules} error: " + e.getMessage());
        }

    }


}
