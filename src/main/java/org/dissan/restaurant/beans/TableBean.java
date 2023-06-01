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
    private final Map<Integer, List<String>> items = new HashMap<>();
    private Table table;
    private OrderCheck check;

    private List<MealItem> cart = new ArrayList<>();

    public void setMealItem(List<MealItem> items) {
        this.mealItems = items;
    }

    public String getTableInfo() {
        return "table: " + this.table.getTableCode() + " customers: " + table.getCustomers();
    }

    public String getTableName(){
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


    private MealItem getCurrentItem(String mealName){
        MealItem item = null;

        for (MealItem mi:
             mealItems) {
            if (mi.getName().equalsIgnoreCase(mealName)){
                item = mi;
                break;
            }
        }

        return item;
    }

    @Override
    public void addItem(String item) {
        try {
            int i = Integer.parseInt(item);
            this.cart.add(getCurrentItem(this.items.get(i).get(0)));
        }catch (NumberFormatException ignored){
            this.cart.add(getCurrentItem(item));
        }
    }

    public List<MealItem> getCart() {
        return cart;
    }

    @Override
    public List<String> getCurrentCart() {
        List<String> arrayList = new ArrayList<>();
        for (MealItem m:
             this.cart) {
            arrayList.add(m.getName());
        }
        return arrayList;
    }

    public OrderCheck getCheck() {
        return check;
    }

    public void setCheck(OrderCheck chk) {
        this.check = chk;
    }

    public void setTable(Table tbl) {
        this.table = tbl;
    }

    public Table getTable() {
        return table;
    }
}
