package org.dissan.restaurant.controllers.api;

import java.util.List;

public interface CustomerOrderApi {

    void sendOrder();
    void pay();
    List<String> getFreeTables();

    String printFreeTables();

}
