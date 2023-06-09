package org.dissan.restaurant.controllers.api;

public interface AttendantOrderApi extends OrderEmployeeApi {
    void setDelivered();
    void checkBill();
}
