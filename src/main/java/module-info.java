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
    requires org.mapstruct;

    exports at.fhtw.tourplanner.ui;
    exports at.fhtw.tourplanner.ui.controller;
    exports at.fhtw.tourplanner.ui.controller.tour;
    exports at.fhtw.tourplanner.ui.model;
    exports at.fhtw.tourplanner.ui.view;
    exports at.fhtw.tourplanner.ui.mapper;
    exports at.fhtw.tourplanner.ui.dto.tour.response;
    exports at.fhtw.tourplanner.ui.dto.tour.request;
    exports at.fhtw.tourplanner.ui.dto.tourlog.response;
    exports at.fhtw.tourplanner.ui.dto.tourlog.request;

    opens at.fhtw.tourplanner.ui to javafx.fxml;
    opens at.fhtw.tourplanner.ui.mapper to org.mapstruct;
    opens at.fhtw.tourplanner.ui.dto.tour.response to com.fasterxml.jackson.databind;
    opens at.fhtw.tourplanner.ui.dto.tour.request to com.fasterxml.jackson.databind;
    opens at.fhtw.tourplanner.ui.dto.tourlog.response to com.fasterxml.jackson.databind;
    opens at.fhtw.tourplanner.ui.dto.tourlog.request to com.fasterxml.jackson.databind;
}