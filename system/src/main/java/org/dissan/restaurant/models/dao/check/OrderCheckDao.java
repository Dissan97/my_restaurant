package org.dissan.restaurant.models.dao.check;

import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.OrderCheck;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

public class OrderCheckDao {
    private static final String CHECKS = "orderChecks.json";
    private static final String TABLE_NAME = "tableName";
    private static final String BILL = "bill";
    private static final String DATE_TIME = "dateTime";
    private static final String MEAL_ITEMS = "mealItems";

    private OrderCheckDao(){}

    /**
     * method to store order check in persistence layer
     * @param orderCheck is the check that must be stored
     */
    public static void storeCheck(@NotNull OrderCheck orderCheck){
        JSONObject object = new JSONObject();
                JSONArray array = loadChecksArray();
            object.put(TABLE_NAME, orderCheck.getTable().getTableCode());
            object.put(BILL, orderCheck.getBill());
            object.put(DATE_TIME, orderCheck.getDateTime());

            JSONArray mealItems = new JSONArray();

        for (MealItem mi:
                    orderCheck.getMealItems()) {
            mealItems.put(mi.getName());
        }

        object.put(MEAL_ITEMS, mealItems);
        assert array != null;
        array.put(object);

        try(BufferedWriter writer = new BufferedWriter(
                new FileWriter(Objects.requireNonNull(OrderCheckDao.class.getResource(OrderCheckDao.CHECKS)).getPath())
        )) {
            array.write(writer);
        }catch (IOException e){
            Logger.getLogger(OrderCheckDao.class.getSimpleName()).warning(e.getMessage());
        }

    }

    private static @Nullable JSONArray loadChecksArray(){
        try (InputStream reader = Objects.requireNonNull(OrderCheckDao.class.getResourceAsStream(OrderCheckDao.CHECKS))){
            JSONTokener jsonTokener = new JSONTokener(reader);
            return  new JSONArray(jsonTokener);
        }catch (IOException e){
            return null;
        }
    }

}
