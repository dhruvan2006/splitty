package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Pair;

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
    private OverviewCtrl overviewCtrl;
    private Scene overview;

    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void create(){
        if(!validateCreate()) return;
        String createEventText = createEventTextField.getText().trim();
        Event newEvent = new Event(createEventText);
        Event added;
        try {
            added = server.addEvent(newEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        giveToOverview();
        overviewCtrl.setCurrent(added);
        overviewCtrl.show();
    }

    public void join(){
        if (validateJoin()) return;
        String inviteCode = joinEventTextField.getText().trim();

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


    public boolean validateCreate(){
        boolean valid = true;
        if(createEventTextField.getText().isEmpty()){
            valid = false;
            createEventTextField.setStyle("-fx-border-color: #E80C0C");
            joinEventTextField.setStyle("-fx-border-color: gray");
        }
        return valid;
    }


    public boolean validateJoin(){
        boolean valid = true;
        if(joinEventTextField.getText().isEmpty()){
            valid = false;
            joinEventTextField.setStyle("-fx-border-color: #E80C0C");
            createEventTextField.setStyle("-fx-border-color: gray");
        }
        return valid;
    }
    public void initialize(Pair<OverviewCtrl, Parent> po) {
        overviewCtrl = po.getKey();
        overview = new Scene(po.getValue());
    }
    public void show(){}
    public void giveToOverview(){overviewCtrl.setStart(this); overviewCtrl.setThisScene(overview);}
}
