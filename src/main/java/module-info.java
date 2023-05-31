module Resturant{
    requires org.jetbrains.annotations;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.json;
    requires java.sql;

    exports org.dissan.restaurant;
    exports org.dissan.restaurant.beans;
    exports org.dissan.restaurant.beans.api;
    exports org.dissan.restaurant.cli.utils;
    exports org.dissan.restaurant.controllers;
    exports org.dissan.restaurant.controllers.api;
    exports org.dissan.restaurant.controllers.exceptions;
    exports org.dissan.restaurant.controllers.util;
    exports org.dissan.restaurant.fxml.controllers;
    exports org.dissan.restaurant.models;
    exports org.dissan.restaurant.models.dao;
    exports org.dissan.restaurant.models.dao.meal;
    exports org.dissan.restaurant.models.dao.schedule;
    exports org.dissan.restaurant.models.dao.shift;
    exports org.dissan.restaurant.models.dao.table;
    exports org.dissan.restaurant.models.dao.user;
    exports org.dissan.restaurant.models.exceptions;
    exports org.dissan.restaurant.patterns.creational.factory;
    exports org.dissan.restaurant.patterns.behavioral.state.cli;
    exports org.dissan.restaurant.patterns.behavioral.state.cli.exceptions;
    exports org.dissan.restaurant.patterns.behavioral.observer.subjects;
    exports org.dissan.restaurant.patterns.behavioral.observer;


    opens org.dissan.restaurant.fxml.controllers to javafx.fxml;
}