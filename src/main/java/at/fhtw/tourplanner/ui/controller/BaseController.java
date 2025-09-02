package at.fhtw.tourplanner.ui.controller;

import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;

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

    protected boolean showSaveButton() {
        return getViewMode().isCreateOrEdit();
    }

    private ViewMode getViewMode() {
        return viewModeService.getViewMode().get();
    }
}
