module restaurant {
    requires org.jetbrains.annotations;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.json;
    requires java.sql;
    requires org.mariadb.jdbc;

    exports org.dissan.restaurant;
    exports org.dissan.restaurant.beans;
    exports org.dissan.restaurant.beans.api;
    exports org.dissan.restaurant.cli.utils;
    exports org.dissan.restaurant.controllers;
    exports org.dissan.restaurant.controllers.api;
    exports org.dissan.restaurant.controllers.exceptions;
    exports org.dissan.restaurant.controllers.util;
    exports org.dissan.restaurant.gui.fxml.controllers;
    exports org.dissan.restaurant.models;
    exports org.dissan.restaurant.models.dao.meal;
    exports org.dissan.restaurant.models.dao.schedule;
    exports org.dissan.restaurant.models.dao.shift;
    exports org.dissan.restaurant.models.dao.table;
    exports org.dissan.restaurant.models.dao.user;
    exports org.dissan.restaurant.models.exceptions;
    exports org.dissan.restaurant.patterns.creational.factory;
    exports org.dissan.restaurant.cli.patterns.behavioral.state.exceptions;
    exports org.dissan.restaurant.patterns.behavioral.observer.subjects;
    exports org.dissan.restaurant.patterns.behavioral.observer;
    exports org.dissan.restaurant.patterns.structural.facade;
    exports org.dissan.restaurant.cli.patterns.behavioral.state;
    exports org.dissan.restaurant.cli.patterns.behavioral.creational.factory;

    opens org.dissan.restaurant.gui.fxml.controllers to javafx.fxml;

}