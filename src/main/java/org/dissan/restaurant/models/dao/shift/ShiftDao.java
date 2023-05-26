package org.dissan.restaurant.models.dao.shift;

import org.dissan.restaurant.models.Shift;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class Dao (Persistence)
 * Information Expert of {Shift, JSONObject, JsonArray};
 * Get information by object JSON in this class I assume that library JSON is stable
 */

public class ShiftDao {

    //variable to switch between local and dbms
    private boolean local = true;

    public ShiftDao(boolean lcl) {
        this.local = lcl;
    }

    public ShiftDao() {
        this(true);
    }

    /**
     * Get all shift list stored in local or database
     * @return List<Shift> loaded from persistence layer
     */
    public List<Shift> getShiftList(){
        JSONObject object;
        List<Shift> shiftList = new ArrayList<>();
        if (local){
            object = ShiftDaoFs.getShiftList();
        }else {
            ShiftDaoDb.init();
            object = ShiftDaoDb.getShiftList();
        }

        if (object != null) {
            Set<String> keySet = object.keySet();
            for (String k :
                    keySet) {
                JSONArray array = object.getJSONArray(k);
                shiftList.add(new Shift(k, array.getString(0), array.getString(1)));
            }
        }
        return shiftList;

    }

    public Shift getShiftByCode(String shiftCode) {
        List<Shift> shiftList = this.getShiftList();
        for (Shift s:
             shiftList) {
            if (s.getCode().equals(shiftCode)){
                return s;
            }
        }
        return null;
    }
}
