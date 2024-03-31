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
import javafx.scene.input.MouseEvent;
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
    ObservableList<Event> observableEvents;

    private final String borderColor = "-fx-border-color: rgb(182,180,180)";


    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    public void initialize(){
        List<Event> events = List.of();
        observableEvents = FXCollections.observableArrayList(events);
        if (recentlyViewedEvents == null){
            recentlyViewedEvents = new ListView<>();
        }
        recentlyViewedEvents.setItems(observableEvents);
        createEventTextField.setStyle(borderColor);
        joinEventTextField.setStyle(borderColor);
    }


    public void create(){
        if(!validateCreate()) return;
        String createEventText = createEventTextField.getText().trim();
        Event newEvent = new Event(createEventText);
        try {
            newEvent = server.addEvent(newEvent);
            clearFields();
            mainCtrl.showOverviewWithEvent(newEvent);
            observableEvents.addFirst(newEvent);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void join(){
        if (!validateJoin()) return;
        Event event;
        String inviteCode = joinEventTextField.getText().trim();
        try{
            List<Event> eventFromServer = server.getEventByInviteCode(inviteCode);
            if(eventFromServer.size() > 1){
                System.out.println("There is a problem on inviteCode");
                return;
            }
            if(eventFromServer.isEmpty()){
                joinEventTextField.setStyle("-fx-border-color: #E80C0C");
                System.out.println("Event does not exists");
                createEventTextField.setStyle(borderColor);
                return;
            }
            System.out.println(eventFromServer);
            event = eventFromServer.get(0);
        }
        catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            joinEventTextField.setStyle("-fx-border-color: #E80C0C");
            System.out.println("Event does not exists");
            createEventTextField.setStyle(borderColor);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Event does not exists \n " +
                            "Please enter a valid invite code");
            alert.showAndWait();
            return;
        }

        clearFields();
        observableEvents.remove(event);
        observableEvents.addFirst(event);
//        System.out.println(event.getExpenses());
        mainCtrl.showOverviewWithEvent(event);

    }

    private void clearFields() {
        createEventTextField.clear();
        joinEventTextField.clear();
        createEventTextField.setStyle(borderColor);
        joinEventTextField.setStyle(borderColor);
    }

    public boolean validateCreate(){
        boolean valid = true;
        if(createEventTextField.getText().isEmpty()){
            valid = false;
            createEventTextField.setStyle("-fx-border-color: #E80C0C");
            joinEventTextField.setStyle(borderColor);
        }
        return valid;
    }


    public boolean validateJoin(){
        boolean valid = true;
        if(joinEventTextField.getText().isEmpty()){
            valid = false;
            joinEventTextField.setStyle("-fx-border-color: #E80C0C");
            createEventTextField.setStyle(borderColor);
        }
        return valid;
    }

    public void updateRecentEvents() {
        OverviewCtrl overviewCtrl = mainCtrl.getOverviewCtrl();
        Event currentEvent = overviewCtrl.getEvent();
        if (currentEvent == null) return;
        for (Event event: observableEvents){
            if (event.getId() == currentEvent.getId()){
                int index = observableEvents.indexOf(event);
                observableEvents.remove(event);
                observableEvents.add(index, currentEvent);
            }
            break;
        }
    }

    @FXML
    public void handleRecentEventClick(MouseEvent arg0) {
        Event event = recentlyViewedEvents.getSelectionModel().getSelectedItem();
        if (event == null) return;
        observableEvents.remove(event);
        observableEvents.addFirst(event);
        mainCtrl.showOverviewWithEvent(event);
    }
}
