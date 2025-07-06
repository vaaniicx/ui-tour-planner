package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.Difficulty;
import at.fhtw.tourplanner.ui.model.Rating;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.viewmodel.TourLogDetailsViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class TourLogDetailsController implements Initializable {

    private TourLogDetailsViewModel viewModel;
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public Text viewTitle;
    public DatePicker date;
    public TextArea comment;
    public ChoiceBox<Difficulty> difficulty;
    public TextField distance;
    public ChoiceBox<Rating> rating;

    public Button saveButton;
    public Button deleteButton;
    public Button editButton;

    private static final Map<Difficulty, String> DIFFICULTY_TYPE_OPTIONS = Map.of(
            Difficulty.EASY, "Leicht",
            Difficulty.MEDIUM, "Mittel",
            Difficulty.HARD, "Schwer"
    );

    private static final Map<Rating, String> RATING_TYPE_OPTIONS = Map.of(
            Rating.PERFECT, "Ausgezeichnet",
            Rating.GOOD, "Gut",
            Rating.BAD, "Schlecht"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = new TourLogDetailsViewModel();

        bindViewTitle();
        bindFormFieldValues();
        setDifficultyItemsAndConverter();
        setRatingItemsAndConverter();
        bindFormFieldReadOnly();
        bindButtonVisibility();
        setButtonOnAction();
    }

    private void bindViewTitle() {
        viewTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            ViewMode viewMode = viewModel.getViewMode().get();
            return switch (viewMode) {
                case CREATE -> "Eintrag erstellen";
                case EDIT -> viewModel.getSelectedTourLog().get() == null ? "Eintrag erstellen" : "Eintrag ändern";
                case READ_ONLY -> "Eintrag ansehen";
            };
        }, viewModel.getViewMode()));
    }

    private void bindFormFieldValues() {
        Bindings.bindBidirectional(date.valueProperty(), viewModel.getDate());
        Bindings.bindBidirectional(comment.textProperty(), viewModel.getComment());
        distance.textProperty().bind(Bindings.createStringBinding(() -> {
            double distanceInKilometers = viewModel.getDistance().get() / 1000;
            return String.format("%.2f km", distanceInKilometers);
        }, viewModel.getDistance()));
        difficulty.valueProperty().bindBidirectional(viewModel.getDifficulty());
        rating.valueProperty().bindBidirectional(viewModel.getRating());
    }

    private void setDifficultyItemsAndConverter() {
        difficulty.setItems(viewModel.getDifficultyTypes());
        difficulty.setConverter(new StringConverter<>() {
            @Override
            public String toString(Difficulty difficulty) {
                return difficulty == null ? "" : DIFFICULTY_TYPE_OPTIONS.get(difficulty);
            }

            @Override
            public Difficulty fromString(String s) {
                return DIFFICULTY_TYPE_OPTIONS.entrySet().stream()
                        .filter(e -> e.getValue().equals(s))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void setRatingItemsAndConverter() {
        rating.setItems(viewModel.getRatingTypes());
        rating.setConverter(new StringConverter<>() {
            @Override
            public String toString(Rating rating) {
                return rating == null ? "" : RATING_TYPE_OPTIONS.get(rating);
            }

            @Override
            public Rating fromString(String s) {
                return RATING_TYPE_OPTIONS.entrySet().stream()
                        .filter(e -> e.getValue().equals(s))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void bindFormFieldReadOnly() {
        BooleanBinding readOnly = viewModel.getViewMode().isEqualTo(ViewMode.READ_ONLY);
        date.disableProperty().bind(readOnly);
        comment.disableProperty().bind(readOnly);
        rating.disableProperty().bind(readOnly);
        difficulty.disableProperty().bind(readOnly);
    }

    private void bindButtonVisibility() {
        saveButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showSaveButton, viewModel.getViewMode()));
        editButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showEditButton, viewModel.getViewMode()));
        deleteButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showDeleteButton, viewModel.getViewMode()));
    }

    private void setButtonOnAction() {
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
