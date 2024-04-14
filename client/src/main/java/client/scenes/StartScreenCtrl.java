package client.scenes;

import client.Main;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


public class StartScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Main main;
    @FXML
    public ComboBox<String> languageDropdown;

    @FXML
    private TextField createEventTextField;

    @FXML
    private TextField joinEventTextField;
    @FXML
    private ListView<Event> recentlyViewedEvents;
    ObservableList<Event> observableEvents;

    private final String borderColor = "-fx-border-color: rgb(182,180,180)";


    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Main main) {
        this.main = main;
        this.mainCtrl = mainCtrl;
        this.server = server;
        server.connectWebSocket();
        server.registerForMessages("/topic/event", Event.class, this::updateRecentEvents);
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
        languageDropdown.getItems().addAll(
                List.of("en", "nl")
        );
        languageDropdown.setCellFactory(e -> new ListCell<String>() {
            private final ImageView view = new ImageView();
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if(empty) {
                    setGraphic(null);
                }
                else {
                    view.setImage(new Image(String.format("images/%s.jpg", name)));
                    view.setFitWidth(30);
                    view.setFitHeight(20);
                    setGraphic(view);
                    setText(name);
                }
            }
        });
    }


    public void create(){
        if(!validateCreate()) return;
        String createEventText = createEventTextField.getText().trim();
        Event newEvent = new Event(createEventText);
        try {
            newEvent = server.addEvent(newEvent);
            server.send("/app/websocket/notify/event", newEvent);
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
        try{
            Event updatedEvent = server.updateLastUsedDate(event.getId());
            server.send("/app/websocket/notify/event", updatedEvent);
        }
        catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            joinEventTextField.setStyle("-fx-border-color: #E80C0C");
            createEventTextField.setStyle(borderColor);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            return;
        }
        observableEvents.remove(event);
        observableEvents.addFirst(event);
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

    public void updateRecentEvents(Event eventUpdate) {
        Platform.runLater(() -> {
            // If event is already in the list
            for (Event event : observableEvents) {
                if (event.getId() == eventUpdate.getId()) {
                    observableEvents.remove(event);
                    observableEvents.add(0, eventUpdate);
                    return;
                }
            }
        });
    }

    @FXML
    public void handleRecentEventClick(MouseEvent arg0) {
        Event event = recentlyViewedEvents.getSelectionModel().getSelectedItem();
        if (event == null) return;
        try{
            Event updatedEvent = server.updateLastUsedDate(event.getId());
            server.send("/app/websocket/notify/event", updatedEvent);
        }
        catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            return;
        }
        observableEvents.remove(event);
        observableEvents.addFirst(event);
        mainCtrl.showOverviewWithEvent(event);
    }

    public void changeLanguage(ActionEvent actionEvent) throws IOException {
        Properties props = new Properties();
        String fileName = "client/config.properties";
        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        }
        try (FileOutputStream out = new FileOutputStream(fileName))
        {
            if (Objects.equals(props.getProperty("LANGUAGE"), languageDropdown.getValue())) return;
            props.setProperty("LANGUAGE", languageDropdown.getValue());
            props.store(out, null);
            main.start(mainCtrl.getPrimaryStage());
        }
        catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
