package at.fhtw.tourplanner.ui.model;

public enum ViewMode {
    READ_ONLY,
    EDIT,
    CREATE;

    public boolean isReadOnly() {
        return this == READ_ONLY;
    }

    public boolean isCreateOrEdit() {
        return this == CREATE || this == EDIT;
    }
}
