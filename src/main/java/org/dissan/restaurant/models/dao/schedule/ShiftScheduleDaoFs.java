package org.dissan.restaurant.models.dao.schedule;

import org.dissan.restaurant.beans.BeanUtil;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.controllers.exceptions.ShiftScheduleDaoException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

public class ShiftScheduleDaoFs {

    private ShiftScheduleDaoFs(){}
    private static final String SCHEDULES = "schedules.json";
    protected static final  String SHIFT = "shift";
    protected static final  String EMPLOYEE_CODE = "employeeCode";
    protected static final  String SHIFT_DATE = "shiftDate";
    protected static final String UPDATE_REQUEST = "updateRequest";
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

        for (Map.Entry<String, JSONObject> entry:
        LOCAL_CACHE.entrySet()) {
            if (BeanUtil.goodDate(entry.getValue().getString(SHIFT_DATE), true) == null){
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
                String shift = schedule.getString(SHIFT);
                String employeeCode = schedule.getString(EMPLOYEE_CODE);
                String shiftDate = schedule.getString(SHIFT_DATE);
                if (BeanUtil.goodDate(shiftDate, true) != null){
                    LOCAL_CACHE.put(shift + ';' + employeeCode + ';' + shiftDate, schedule);
                }
            }

        }catch (IOException | NullPointerException e){
            OutStream.println("UserDao{loadSchedules} error: " + e.getMessage());
        }

    }


    public static JSONObject pullSchedule(String sCode, String eCode, String dateTime) {
        checkLocalCache();
        return LOCAL_CACHE.get(sCode + ';' + eCode + ';' + dateTime);
    }

    @Contract("_ -> !null")
    private static String getCode(@NotNull JSONObject object){
        return object.getString(SHIFT + ';' + ShiftScheduleDaoFs.EMPLOYEE_CODE + ';' +ShiftScheduleDaoFs.SHIFT_DATE);
    }

    public static void pushSchedule(JSONObject object) throws ShiftScheduleDaoException {
        loadSchedules();
        String code = getCode(object);
        if (LOCAL_CACHE.putIfAbsent(code, object) == null){
            throw new ShiftScheduleDaoException("shift already exist");
        }
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(Objects.requireNonNull(ShiftScheduleDaoFs.class.getResource(SCHEDULES)).getPath())
        )){
            JSONArray array = new JSONArray();
            for (Map.Entry <String,JSONObject> entry:
                 LOCAL_CACHE.entrySet()) {
                array.put(entry.getValue());
            }
            array.write(writer);
        }catch (IOException e){
            OutStream.println("PROBLEMS: " + e.getMessage());
        }

    }
}
