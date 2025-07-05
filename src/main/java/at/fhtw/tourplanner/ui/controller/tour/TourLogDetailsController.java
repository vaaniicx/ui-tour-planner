package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.viewmodel.TourLogDetailsViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogDetailsController implements Initializable {

    public Button deleteButton;
    public Button editButton;
    private TourLogDetailsViewModel viewModel;
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public Text viewTitle;
    public Label dateLabel;
    public DatePicker date;
    public Label commentLabel;
    public TextArea comment;
    public Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = new TourLogDetailsViewModel();

        initializeFormFieldBindings();
        initializeButtonVisibilityBindings();
        registerButtonActions();
    }

    private void initializeFormFieldBindings() {
        Bindings.bindBidirectional(date.valueProperty(), viewModel.getDate());
        Bindings.bindBidirectional(comment.textProperty(), viewModel.getComment());

        viewTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            ViewMode viewMode = viewModel.getViewMode().get();
            return switch (viewMode) {
                case CREATE -> "Eintrag erstellen";
                case EDIT -> viewModel.getSelectedTourLog().get() == null ? "Eintrag erstellen" : "Eintrag ändern";
                case READ_ONLY -> "Eintrag ansehen";
            };
        }, viewModel.getViewMode()));

        BooleanBinding readOnly = viewModel.getViewMode().isEqualTo(ViewMode.READ_ONLY);
        date.disableProperty().bind(readOnly);
        comment.disableProperty().bind(readOnly);
    }

    private void initializeButtonVisibilityBindings() {
        saveButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showSaveButton, viewModel.getViewMode()));
        editButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showEditButton, viewModel.getViewMode()));
        deleteButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showDeleteButton, viewModel.getViewMode()));
    }

    private void registerButtonActions() {
        saveButton.setOnAction(_ -> onSaveButtonClick());
        editButton.setOnAction(_ -> onEditButtonClick());
        deleteButton.setOnAction(_ -> onDeleteButtonClick());
    }

    private void onSaveButtonClick() {
        viewModel.saveTourLog();
    }

    private void onEditButtonClick() {
        viewModel.switchToEditMode();
    }

    private void onDeleteButtonClick() {
        viewModel.deleteTourLog();
    }

    private boolean showSaveButton() {
        ViewMode viewMode = viewModeService.getViewMode().get();
        return viewMode == ViewMode.CREATE || viewMode == ViewMode.EDIT;
    }

    private boolean showEditButton() {
        return isReadOnlyViewMode();
    }

    private boolean showDeleteButton() {
        return isReadOnlyViewMode();
    }

    private boolean isReadOnlyViewMode() {
        return viewModeService.getViewMode().get() == ViewMode.READ_ONLY;
    }
}
