package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.ViewMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class ViewModeService {

    private static ViewModeService instance;

    @Getter
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();

    public static ViewModeService getInstance() {
        if (instance == null) {
            instance = new ViewModeService();
        }
        return instance;
    }
}
