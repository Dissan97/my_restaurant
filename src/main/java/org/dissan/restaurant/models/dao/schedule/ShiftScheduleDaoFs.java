package org.dissan.restaurant.models.dao.schedule;

import org.dissan.restaurant.beans.BeanUtil;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShiftScheduleDaoFs {

    private ShiftScheduleDaoFs(){}
    private static final String SCHEDULES = "schedules.json";

    private static final Map<Integer, JSONObject> LOCAL_CACHE = new HashMap<>();
    public static @Nullable JSONArray pullShiftSchedules() {

        checkLocalCache();

        if (LOCAL_CACHE.isEmpty()){
            return null;
        }

        Set<Integer> keys = LOCAL_CACHE.keySet();
        JSONArray array = new JSONArray();
        int i = 0;
        for (Integer k:
             keys) {
            array.put(i++, LOCAL_CACHE.get(k));
        }

        return array;
    }

    private static void checkLocalCache() {
        if (LOCAL_CACHE.isEmpty()){
            loadSchedules();
        }

        for (Map.Entry<Integer, JSONObject> entry:
        LOCAL_CACHE.entrySet()) {
            if (BeanUtil.goodDate(entry.getValue().getString(ShiftScheduleDao.SHIFT_DATE), true) == null){
                loadSchedules();
            }
        }

    }


    private static void loadSchedules(){
        JSONArray schedulesArray;
        LOCAL_CACHE.clear();
        try (InputStream reader = Objects.requireNonNull(ShiftScheduleDaoFs.class.getResourceAsStream(ShiftScheduleDaoFs.SCHEDULES))){
            JSONTokener jsonTokener = new JSONTokener(reader);
            schedulesArray = new JSONArray(jsonTokener);
            for (int i = 0; i < schedulesArray.length(); i++){
                JSONObject schedule = schedulesArray.getJSONObject(i);
                String shiftDate = schedule.getString(ShiftScheduleDao.SHIFT_DATE);
                if (BeanUtil.goodDate(shiftDate, true) != null){
                    LOCAL_CACHE.put(i, schedule);
                }
            }

        }catch (IOException | NullPointerException e){
            OutStream.println("UserDao{loadSchedules} error: " + e.getMessage());
        }

    }


    public static @Nullable JSONObject pullSchedule(String sCode, String eCode, String dateTime) {
        checkLocalCache();
        for (Map.Entry<Integer, JSONObject> entry:
             LOCAL_CACHE.entrySet()) {
            JSONObject object = entry.getValue();

            if (object.getString(ShiftScheduleDao.SHIFT).equals(sCode) && object.getString(ShiftScheduleDao.EMPLOYEE_CODE).equals(eCode)
                && object.getString(ShiftScheduleDao.SHIFT_DATE).equals(dateTime)){
                return object;
            }
        }
        return null;
    }

    public static void pushSchedule(JSONObject object) throws ShiftScheduleDaoException{
        pushSchedule(object, false, false);
    }

    public static void pushSchedule(@NotNull JSONObject object, boolean update, boolean accepted) throws ShiftScheduleDaoException {

        String sCode = object.getString(ShiftScheduleDao.SHIFT);
        String eCode = object.getString(ShiftScheduleDao.EMPLOYEE_CODE);
        String date = object.getString(ShiftScheduleDao.SHIFT_DATE);



        if (ShiftScheduleDaoFs.pullSchedule(sCode, eCode, date) != null) {
            if (update) {
                removeObject(sCode, eCode, date);
                if (accepted){
                    object.remove(ShiftScheduleDao.SHIFT_DATE);
                    object.put(ShiftScheduleDao.SHIFT_DATE, object.get(ShiftScheduleDao.SHIFT_UPDATE_DATE));
                    object.remove(ShiftScheduleDao.SHIFT_UPDATE_DATE);
                }
            }
        }

        LOCAL_CACHE.put(LOCAL_CACHE.size(), object);
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(Objects.requireNonNull(ShiftScheduleDaoFs.class.getResource(SCHEDULES)).getPath())
        )){
            JSONArray array = new JSONArray();
            for (Map.Entry <Integer,JSONObject> entry:
                 LOCAL_CACHE.entrySet()) {
                array.put(entry.getValue());
            }
            array.write(writer);
        }catch (IOException e){
            throw new ShiftScheduleDaoException("PROBLEMS: " + e.getMessage());
        }

    }

    private static void removeObject(@NotNull String sCode,@NotNull String eCode, @NotNull String date) {
        if (LOCAL_CACHE.isEmpty()){
            loadSchedules();
        }

        int index = -1;

        for (Map.Entry<Integer, JSONObject> entry:
             LOCAL_CACHE.entrySet()) {
               JSONObject object = entry.getValue();
               if ( object.getString(ShiftScheduleDao.SHIFT).equals(sCode) &&
                    object.getString(ShiftScheduleDao.EMPLOYEE_CODE).equals(eCode) &&
                    object.getString(ShiftScheduleDao.SHIFT_DATE).equals(date)){
                   index = entry.getKey();
               }
        }

        if (index != -1){
            LOCAL_CACHE.remove(index);
        }
    }


    public static void update(JSONObject object, boolean accepted) throws ShiftScheduleDaoException {
        pushSchedule(object, true, accepted);
    }
}
