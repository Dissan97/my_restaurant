package org.dissan.restaurant.fxml.controllers;

import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;

public class CustomerOrderControllerGui {
    private CustomerOrderFacade orderFacade;

    public void setFacade(CustomerOrderFacade facade) {
        this.orderFacade = facade;
    }
}
