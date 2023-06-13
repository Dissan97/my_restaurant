module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires restaurant;
    requires org.jetbrains.annotations;
    requires java.logging;

    exports org.dissan.restaurant;
    exports org.dissan.restaurant.fxml.controllers;

    opens org.dissan.restaurant.fxml.controllers to javafx.fxml;
}