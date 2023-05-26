package org.dissan.restaurant.beans;

import org.dissan.restaurant.beans.api.TableBeanApi;
import org.dissan.restaurant.models.MealItem;
import org.dissan.restaurant.models.OrderCheck;
import org.dissan.restaurant.models.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBean implements TableBeanApi {
    private List<MealItem> mealItems;
    private Map<Integer, List<String>> items = new HashMap<>();
    private Table table;
    private OrderCheck check;

    private List<MealItem> currentCart = new ArrayList<>();
    public TableBean(Table tbl) {
        this.table = tbl;
        this.check = this.table.getCheck();
    }

    public void setMealItem(List<MealItem> items) {
        this.mealItems = items;
    }

    public String getTable() {
        return this.table.getTableCode();
    }

    @Override
    public Map<Integer, List<String>> getItems() {
        if (items.isEmpty()) {
            int i = 1;
            for (MealItem mi :
                    this.mealItems) {
                List<String> info = new ArrayList<>();
                info.add(mi.getName());
                info.add(String.valueOf(mi.getPrice()));
                info.addAll(mi.getIngredients());
                items.put(i++, info);
            }
        }
        return items;
    }

    @Override
    public void addItem(String item) {
        for (MealItem m:
             this.mealItems) {
            if (m.getName().equals(item)){
                this.currentCart.add(m);
            }
        }
    }

    @Override
    public List<String> getCurrentCart() {
        List<String> arrayList = new ArrayList<>();
        for (MealItem m:
             this.currentCart) {
            arrayList.add(m.getName());
        }
        return arrayList;
    }
}
