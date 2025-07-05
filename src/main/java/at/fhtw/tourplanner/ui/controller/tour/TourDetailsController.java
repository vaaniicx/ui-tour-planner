package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.viewmodel.TourDetailsViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsController implements Initializable {

    private TourDetailsViewModel viewModel;
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public Text viewTitle;
    public Label nameLabel;
    public Label descriptionLabel;
    public TextField name;
    public TextArea description;
    public TextField from;
    public TextField to;
    public Button saveButton;
    public Button editButton;
    public Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = new TourDetailsViewModel();
        viewModel.switchToCreateMode();

        initializeFormFieldBindings();
        initializeButtonVisibilityBindings();
        registerButtonActions();
    }

    private void initializeFormFieldBindings() {
        Bindings.bindBidirectional(name.textProperty(), viewModel.getName());
        Bindings.bindBidirectional(description.textProperty(), viewModel.getDescription());
        Bindings.bindBidirectional(from.textProperty(), viewModel.getFrom());
        Bindings.bindBidirectional(to.textProperty(), viewModel.getTo());

        viewTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            ViewMode viewMode = viewModel.getViewMode().get();
            return switch (viewMode) {
                case CREATE -> "Tour erstellen";
                case EDIT -> "Tour ändern";
                case READ_ONLY -> "Tour ansehen";
            };
        }, viewModel.getViewMode()));

        BooleanBinding readOnly = viewModel.getViewMode().isEqualTo(ViewMode.READ_ONLY);
        name.disableProperty().bind(readOnly);
        description.disableProperty().bind(readOnly);
        from.disableProperty().bind(readOnly);
        to.disableProperty().bind(readOnly);
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
        viewModel.saveTour();
    }

    private void onEditButtonClick() {
        viewModel.switchToEditMode();
    }

    private void onDeleteButtonClick() {
        viewModel.deleteTour();
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
