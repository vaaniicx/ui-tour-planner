package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.Location;
import at.fhtw.tourplanner.ui.model.TransportType;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.viewmodel.TourDetailsViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class TourDetailsController implements Initializable {

    private TourDetailsViewModel viewModel;
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public Text viewTitle;
    public WebView mapView;
    public TextField name;
    public TextArea description;
    public Button saveButton;
    public Button editButton;
    public Button deleteButton;

    public ChoiceBox<TransportType> transportTypes;
    private static final Map<TransportType, String> TRANSPORT_TYPE_OPTIONS = Map.of(
            TransportType.CAR, "Auto",
            TransportType.BIKE, "Fahrrad",
            TransportType.WALK, "Zu Fuß",
            TransportType.HIKE, "Wandern",
            TransportType.WHEELCHAIR, "Rollstuhl"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = new TourDetailsViewModel();
        viewModel.switchToCreateMode();

        loadLeafletMap();
        initializeFormFieldBindings();
        initializeButtonVisibilityBindings();
        registerButtonActions();
    }

    private void loadLeafletMap() {
        WebEngine engine = mapView.getEngine();
        engine.load(Objects.requireNonNull(getClass().getResource("/view/leaflet/map.html")).toExternalForm());
    }

    private void initializeFormFieldBindings() {
        Bindings.bindBidirectional(name.textProperty(), viewModel.getName());
        Bindings.bindBidirectional(description.textProperty(), viewModel.getDescription());

        viewTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            ViewMode viewMode = viewModel.getViewMode().get();
            return switch (viewMode) {
                case CREATE -> "Tour erstellen";
                case EDIT -> "Tour ändern";
                case READ_ONLY -> "Tour ansehen";
            };
        }, viewModel.getViewMode()));

        transportTypes.setItems(viewModel.getTransportTypes());
        transportTypes.setConverter(new StringConverter<>() {
            @Override
            public String toString(TransportType transportType) {
                return transportType == null ? "" : TRANSPORT_TYPE_OPTIONS.get(transportType);
            }

            @Override
            public TransportType fromString(String s) {
                return TRANSPORT_TYPE_OPTIONS.entrySet().stream()
                        .filter(e -> e.getValue().equals(s))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            }
        });

        transportTypes.valueProperty().bindBidirectional(viewModel.getTransportType());

        BooleanBinding readOnly = viewModel.getViewMode().isEqualTo(ViewMode.READ_ONLY);
        name.disableProperty().bind(readOnly);
        description.disableProperty().bind(readOnly);
        transportTypes.disableProperty().bind(readOnly);
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
        WebEngine engine = mapView.getEngine();

        String fromLat = (String) engine.executeScript("document.getElementById('from-lat').value");
        String fromLng = (String) engine.executeScript("document.getElementById('from-lng').value");
        String toLat = (String) engine.executeScript("document.getElementById('to-lat').value");
        String toLng = (String) engine.executeScript("document.getElementById('to-lng').value");

        viewModel.getFrom().set(new Location(fromLat, fromLng));
        viewModel.getTo().set(new Location(toLat, toLng));
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
