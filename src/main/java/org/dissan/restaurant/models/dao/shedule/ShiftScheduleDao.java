package org.dissan.restaurant.models.dao.shedule;

import org.dissan.restaurant.models.Shift;
import org.dissan.restaurant.models.ShiftSchedule;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ShiftScheduleDao {

    private boolean local = true;

    public ShiftScheduleDao(boolean lcl) {
        this.local = lcl;
    }

    public ShiftScheduleDao() {
        this(true);
    }

    public void update(@NotNull ShiftSchedule schedule) {

    }

    public void pushShiftSchedule(@NotNull ShiftSchedule schedule){

    }

    public List<ShiftSchedule> pullShiftSchedules(){
        List<ShiftSchedule> shiftScheduleList = new ArrayList<>();
        JSONArray array = null;
        if (local){
            array = ShiftScheduleDaoFs.pullShiftSchedules();
        }else {
            array = ShiftScheduleDaoDb.pullShiftSchedules();
        }

        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                String key = array.getString(i);
                String[] keys = key.split(";");
                ShiftSchedule schedule = new ShiftSchedule(keys[0], keys[1], keys[2]);
                schedule.setUpdate(array.getJSONObject(i).getBoolean(key));
                shiftScheduleList.add(schedule);
            }
        }

        return shiftScheduleList;
    }

    public List<ShiftSchedule> getShiftUpdateRequest() {
        return new ArrayList<>();
    }

    public ShiftSchedule getShiftByKey(String sCode, String eCode, String dateTime) {

        return null;
    }

}
