package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.controller.BaseController;
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

public class TourDetailsController extends BaseController implements Initializable {

    private TourDetailsViewModel viewModel;
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public Text viewTitle;
    public WebView mapView;
    public TextField name;
    public TextArea description;
    public TextField distance;
    public TextField duration;
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
        switchToCreateMode();

        loadLeafletMap();
        bindViewTitle();
        bindFormFieldValues();
        setTransportTypeItemsAndConverter();
        bindFormFieldReadOnly();
        bindButtonVisibility();
        setButtonOnAction();

        viewModel.getSelectedTour().addListener((_, _, tour) -> {
            if (tour != null) {
                showRouteMarkers();
            } else {
                clearRouteMarkers();
            }
        });

        viewModel.getViewMode().addListener((_, _, _) -> disableRouteMarkers());
    }

    private void showRouteMarkers() {
        WebEngine engine = mapView.getEngine();

        Location from = viewModel.getSelectedTour().get().from();
        Location to = viewModel.getSelectedTour().get().to();
        String js = String.format("setRoute(%s, %s, %s, %s);", from.latitude(), from.longitude(), to.latitude(), to.longitude());
        engine.executeScript(js);
    }

    private void clearRouteMarkers() {
        WebEngine engine = mapView.getEngine();
        engine.executeScript("clearMarkers();");
    }

    private void disableRouteMarkers() {
        WebEngine engine = mapView.getEngine();
        boolean readOnly = viewModel.getViewMode().get() == ViewMode.READ_ONLY;
        engine.executeScript("setIsReadOnly(" + readOnly + ");");
    }

    private void loadLeafletMap() {
        WebEngine engine = mapView.getEngine();
        engine.load(Objects.requireNonNull(getClass().getResource("/view/leaflet/map.html")).toExternalForm());
    }

    private void bindViewTitle() {
        viewTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            ViewMode viewMode = viewModel.getViewMode().get();
            return switch (viewMode) {
                case CREATE -> "Tour erstellen";
                case EDIT -> "Tour ändern";
                case READ_ONLY -> "Tour ansehen";
            };
        }, viewModel.getViewMode()));
    }

    private void bindFormFieldValues() {
        Bindings.bindBidirectional(name.textProperty(), viewModel.getName());
        Bindings.bindBidirectional(description.textProperty(), viewModel.getDescription());
        transportTypes.valueProperty().bindBidirectional(viewModel.getTransportType());
        distance.textProperty().bind(Bindings.createStringBinding(() -> {
            double distanceInKilometers = viewModel.getDistance().get() / 1000;
            return String.format("%.2f km", distanceInKilometers);
        }, viewModel.getDistance()));
        duration.textProperty().bind(Bindings.createStringBinding(() -> {
            int duration = (int) viewModel.getDuration().getValue().doubleValue();

            int minutes = duration / 60;
            int seconds = duration % 60;

            return String.format("%02d:%02d min", minutes, seconds);
        }, viewModel.getDuration()));
    }

    private void setTransportTypeItemsAndConverter() {
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
    }

    private void bindFormFieldReadOnly() {
        BooleanBinding readOnly = viewModel.getViewMode().isEqualTo(ViewMode.READ_ONLY);
        name.disableProperty().bind(readOnly);
        description.disableProperty().bind(readOnly);
        transportTypes.disableProperty().bind(readOnly);
    }

    private void bindButtonVisibility() {
        saveButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showSaveButton, viewModel.getViewMode()));
        editButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showEditButton, viewModel.getViewMode()));
        deleteButton.visibleProperty().bind(Bindings.createBooleanBinding(this::showDeleteButton, viewModel.getViewMode()));
    }

    private void setButtonOnAction() {
        saveButton.setOnAction(_ -> onSaveButtonClick());
        editButton.setOnAction(_ -> switchToEditMode());
        deleteButton.setOnAction(_ -> onDeleteButtonClick());
    }

    private void onSaveButtonClick() {
        String fromLat = getFromElementInMapView("from-lat");
        String fromLng = getFromElementInMapView("from-lng");
        String toLat = getFromElementInMapView("to-lat");
        String toLng = getFromElementInMapView("to-lng");

        viewModel.getFrom().set(new Location(fromLat, fromLng));
        viewModel.getTo().set(new Location(toLat, toLng));
        viewModel.saveTour();
    }

    private String getFromElementInMapView(String elementId) {
        return (String) mapView.getEngine().executeScript("document.getElementById('" + elementId + "').value");
    }

    private void onDeleteButtonClick() {
        viewModel.deleteTour();
    }
}
