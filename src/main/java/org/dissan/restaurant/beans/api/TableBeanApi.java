package org.dissan.restaurant.beans.api;

import org.dissan.restaurant.models.MealItem;

import java.util.List;
import java.util.Map;

public interface TableBeanApi {
    String getTable();

    Map<Integer, List<String>> getItems();

    void addItem(String item);

    List<String> getCurrentCart();
}
