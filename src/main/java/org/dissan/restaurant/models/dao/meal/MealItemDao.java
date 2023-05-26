package org.dissan.restaurant.models.dao.meal;

import org.dissan.restaurant.models.MealItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealItemDao {
    private MealItemDao() {
    }

    private static final String MENU = "menu.json";
    private static List<MealItem> mealItemList;

    public static List<MealItem> pullMenuItems() {

        if (mealItemList == null) {
            JSONArray array = pullMenuItemList();
            if (array != null) {
                mealItemList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String name = object.getString("name");
                    double price = object.getDouble("price");
                    JSONArray ingredients = object.getJSONArray("ingredients");
                    List<String> ingredientList = new ArrayList<>();
                    for (int j = 0; j < ingredients.length(); j++) {
                        ingredientList.add(ingredients.getString(j));
                    }
                    MealItem item = new MealItem(name, ingredientList, price);
                    mealItemList.add(item);
                }
            }
        }
        return mealItemList;
    }

    private static JSONArray pullMenuItemList() {
        JSONArray array = null;
        try (InputStream reader = Objects.requireNonNull(MealItemDao.class.getResourceAsStream(MealItemDao.MENU))) {
            JSONTokener jsonTokener = new JSONTokener(reader);
            array = new JSONArray(jsonTokener);
            return array;
        } catch (JSONException | IOException e) {
            return array;
        }
    }
}