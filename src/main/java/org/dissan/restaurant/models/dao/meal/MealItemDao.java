package org.dissan.restaurant.models.dao.meal;

import org.dissan.restaurant.models.MealItem;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String INGREDIENTS = "ingredients";
    public static List<MealItem> pullMenuItems() {

        if (mealItemList == null) {
            JSONArray array = pullMenuItemList();
            if (array != null) {
                mealItemList = new ArrayList<>();
                for (int index = 0; index < array.length(); index++) {
                    mealItemList.add(parseMealItemFromJson(array.getJSONObject(index)));
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

    @Contract("_ -> new")
    public static @NotNull MealItem parseMealItemFromJson(@NotNull JSONObject object) {
        String name = object.getString(NAME);
        double price = object.getDouble(PRICE);
        JSONArray ingredients = object.getJSONArray(INGREDIENTS);
        List<String> ingredientList = new ArrayList<>();
        for (int ingredient = 0; ingredient < ingredients.length(); ingredient++) {
            ingredientList.add(ingredients.getString(ingredient));
        }
        return new MealItem(name, ingredientList, price);
    }

    public static @Nullable JSONObject parseMealItemToJson(@NotNull String mealName) {
        List<MealItem> mealItems = pullMenuItems();
        for (MealItem mi:
             mealItems) {
            if (mi.getName().equals(mealName)){
                return parseMealItemToJson(mi);
            }
        }
        return null;
    }

    public static @NotNull JSONObject parseMealItemToJson(@NotNull MealItem mealItem){
        JSONObject mealItemJson = new JSONObject();

        mealItemJson.put(NAME, mealItem.getName());
        mealItemJson.put(PRICE, mealItem.getPrice());
        JSONArray ingredients = new JSONArray();
        for (String i:
             mealItem.getIngredients()) {
            ingredients.put(i);
        }
        mealItemJson.put(INGREDIENTS, ingredients);

        return mealItemJson;
    }
}