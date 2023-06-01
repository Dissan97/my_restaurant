package org.dissan.restaurant.beans.api;

import java.util.List;
import java.util.Map;

public interface TableBeanApi {
    String getTableInfo();

    Map<Integer, List<String>> getItems();

    void addItem(String item);

    List<String> getCurrentCart();
}
