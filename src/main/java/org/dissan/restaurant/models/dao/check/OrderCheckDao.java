package org.dissan.restaurant.models.dao.check;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.OrderCheck;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.models.dao.table.TableDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderCheckDao {
    private static final String CHECKS = "orderChecks.json";
    private static final String TABLE_NAME = "tableName";
    private static final String BILL = "bill";
    private static final String DATE_TIME = "dateTime";
    private static final String MEAL_ITEMS = "mealItems";

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
            OutStream.println(e.getMessage());
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

    //todo adjust this stuff
    public static @NotNull List<OrderCheck> loadChecks(){
        List<OrderCheck> orderCheckList = new ArrayList<>();
        JSONArray orderCheckArray = loadChecksArray();
        assert orderCheckArray != null;
        try {
            for (int i = 0; i < orderCheckArray.length(); i++) {
                JSONObject object = orderCheckArray.getJSONObject(i);
                String tableName = object.getString(TABLE_NAME);
                double bill = object.getDouble(BILL);
                String dateTime = object.getString(DATE_TIME);
                JSONArray mealItemsJArray = object.getJSONArray(MEAL_ITEMS);
                List<MealItem> mealItemList = new ArrayList<>();
                for (int j = 0; j < mealItemsJArray.length(); j++){
                    String itemName = mealItemsJArray.getString(j);
                    mealItemList.add(MealItemDao.parseMealItemFromJson(Objects.requireNonNull(MealItemDao.parseMealItemToJson(itemName))));
                }
                Table table = TableDao.getTableByName(tableName);
                OrderCheck orderCheck = new OrderCheck(table);
                orderCheck.setBill(bill);
                orderCheck.setMealItems(mealItemList);
                orderCheck.setDateTime(dateTime);
                orderCheckList.add(orderCheck);
            }

        } catch (JSONException  e){
            OutStream.println(e.getMessage());
        }

        return orderCheckList;
    }
}
