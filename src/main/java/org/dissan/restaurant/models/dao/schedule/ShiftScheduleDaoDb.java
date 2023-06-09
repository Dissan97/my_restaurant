package org.dissan.restaurant.models.dao.schedule;

import org.dissan.restaurant.models.ShiftSchedule;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

public class ShiftScheduleDaoDb {

    private ShiftScheduleDaoDb(){}
    @Contract(value = " -> new", pure = true)
    public static @NotNull JSONArray pullShiftSchedules() {
        return new JSONArray();
    }

    public static void update(ShiftSchedule schedule, boolean accepted) {
        //to implement
    }
}
