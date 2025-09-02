package at.fhtw.tourplanner.ui.controller;

import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import javafx.beans.property.ObjectProperty;

public abstract class BaseController {

    protected final ViewModeService viewModeService = ViewModeService.getInstance();

    protected boolean showEditButton() {
        return isReadOnlyViewMode();
    }

    protected boolean showDeleteButton() {
        return isReadOnlyViewMode();
    }

    protected boolean isReadOnlyViewMode() {
        return getViewMode().isReadOnly();
    }

    protected boolean isEditMode() {
        return getViewMode().isEdit();
    }

    protected boolean showSaveButton() {
        return getViewMode().isCreateOrEdit();
    }

    protected void switchToCreateMode() {
        getViewModeProperty().set(ViewMode.CREATE);
    }

    protected void switchToEditMode() {
        getViewModeProperty().set(ViewMode.EDIT);
    }

    protected void switchToReadOnlyMode() {
        getViewModeProperty().set(ViewMode.READ_ONLY);
    }

    private ViewMode getViewMode() {
        return getViewModeProperty().get();
    }

    private ObjectProperty<ViewMode> getViewModeProperty() {
        return viewModeService.getViewMode();
    }
}
