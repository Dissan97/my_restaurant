package org.dissan.restaurant.controllers.api;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.models.MealItem;

public interface CustomerOrderApi {
    void addItem(MealItem item);
    void sendOrder();
    void pay();
    TableBean getMenuBean();
}
