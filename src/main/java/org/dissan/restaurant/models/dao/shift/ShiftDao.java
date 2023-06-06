package org.dissan.restaurant.models.dao.shift;

import org.dissan.restaurant.controllers.util.DBMSException;
import org.dissan.restaurant.models.Shift;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
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
    protected static final String CODE = "code";
    protected static final String TASK = "task";
    protected static final String ROLE = "role";
    protected static final String SALARY = "salary";
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
        JSONArray array = null;
        List<Shift> shiftList = new ArrayList<>();
        if (local){
            array = ShiftDaoFs.getShiftList();
        }else {
            //ShiftDaoDb.init();
            try {
                array = ShiftDaoDb.getShiftList();
            } catch (SQLException | ClassNotFoundException | DBMSException e) {
                //todo adjust this
            }
        }

        if (array != null) {
            for (int i = 0;  i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String code = object.getString(CODE);
                String task = object.getString(TASK);
                String role = object.getString(ROLE);
                double salary = object.getDouble(SALARY);
                shiftList.add(new Shift(code, task, role, salary));
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
