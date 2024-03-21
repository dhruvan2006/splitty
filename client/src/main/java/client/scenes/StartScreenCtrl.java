package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.List;


public class StartScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField createEventTextField;

    @FXML
    private TextField joinEventTextField;
    @FXML
    private ListView<Event> recentlyViewedEvents;


    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void create(){
        //Will code after API is created
        String createEventText = createEventTextField.getText().trim();
        Event newEvent = new Event(createEventText);
        try {
            server.addEvent(newEvent);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showStartScreen();
    }

    public void join(){
        //Will code after API is created
    }
    private Event createEvent(){
        var eventTitle = createEventTextField.getText();
        Event x = new Event(eventTitle);
        return x;
    }

    private void clearFields() {
        createEventTextField.clear();
        joinEventTextField.clear();
    }


    public void updateEventsList() {
        List<Event> events = List.of(new Event("New Year"), new Event("X")); // Hardcoded when API is formed it will be fetched from there
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
        if(recentlyViewedEvents == null){
            recentlyViewedEvents = new ListView<>();
        }
        recentlyViewedEvents.setItems(observableEvents);
    }
}
