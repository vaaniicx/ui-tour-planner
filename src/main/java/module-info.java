module at.fhtw.tourplanner.ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires lombok;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens at.fhtw.tourplanner.ui to javafx.fxml;
    exports at.fhtw.tourplanner.ui;
    exports at.fhtw.tourplanner.ui.controller;
    exports at.fhtw.tourplanner.ui.controller.tour;
    exports at.fhtw.tourplanner.ui.model;
    exports at.fhtw.tourplanner.ui.view;
}