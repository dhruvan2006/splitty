package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Event> data;

    @FXML
    private TableView<Event> table;

    @FXML
    private TableColumn<Event, String> titleColumn;

    @FXML
    private TableColumn<Event, String> creationDateColumn;

    @FXML
    private TableColumn<Event, String> lastActivityColumn;

    @FXML
    private TableColumn<Event, String> deleteColumn;

    @FXML
    private TableColumn<Event, String> jsonColumn;

    @FXML
    private Button importEventButton;

    @Inject
    public AdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleColumn.setCellValueFactory(event -> new SimpleStringProperty(event.getValue().getTitle()));
        creationDateColumn.setCellValueFactory(event -> new SimpleStringProperty(formatDate(event.getValue().getOpenDate())));
        lastActivityColumn.setCellValueFactory(event -> new SimpleStringProperty(formatDate(event.getValue().getLastUsed())));


        deleteColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Event eventToDelete = getTableView().getItems().get(getIndex());
                        // confirmation
                        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
                        confirmDialog.setHeaderText("Confirm Deletion");
                        Optional<ButtonType> result = confirmDialog.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            server.deleteEventById(eventToDelete.getId());
                            refresh();
                            // success
                            mainCtrl.showNotification("Event deleted successfully", "#4CAF50");
                        }
                    });
                }
            }
        });

        jsonColumn.setCellFactory(column -> new TableCell<>() {
            private final Button jsonButton = new Button("JSON");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(jsonButton);
                    jsonButton.setOnAction(event -> {
                        Event eventData = getTableView().getItems().get(getIndex());
                        try {
                            // I am using jackson to generate JSON dump of an event
                            ObjectMapper objectMapper = new ObjectMapper();
                            String json = objectMapper.writeValueAsString(eventData);
                            boolean saved = saveJsonToFile(json, eventData.getTitle());
                            if (saved) {
                                // success
                                mainCtrl.showNotification("Event exported successfully", "#4CAF50");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            // error
                            mainCtrl.showNotification("Event export failed", "#F44336");
                        }
                    });
                }
            }
        });

        refresh();
    }

    private boolean saveJsonToFile(String json, String eventTitle) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON");
        // replace illegal characters
        String sanitizedEventName = eventTitle.replaceAll("[^a-zA-Z0-9.-]", "_");
        fileChooser.setInitialFileName(sanitizedEventName + ".json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(json);
                return true;
            } catch (IOException e) {
                throw e;
            }
        }
        return false;
    }

    public void handleImportEvent() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Event");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String jsonContent = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                importEventFromJSON(jsonContent);
            } catch (IOException e) {
                e.printStackTrace();
                mainCtrl.showNotification("Error importing event", "#F44336");
            }
        }
    }

    private void importEventFromJSON(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Event eventToImport = objectMapper.readValue(jsonContent, Event.class);

            String inviteCode = eventToImport.getInviteCode();
            List<Event> existingEvents = server.getEventByInviteCode(inviteCode);

            if (existingEvents.isEmpty()) {
                var lostExpenses = clearIds(eventToImport);
                Event newEvent = server.addEvent(eventToImport);
                addExpenses(lostExpenses, newEvent);
                refresh();
                mainCtrl.showNotification("Event imported successfully", "#4CAF50");
            } else {
                handleInviteCodeConflict(eventToImport);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mainCtrl.showNotification("Error importing event", "#F44336");
        }
    }

    private void handleInviteCodeConflict(Event eventToImport) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Invite Code Conflict");
        alert.setHeaderText("An event with the same invite code already exists.");
        alert.setContentText("Do you want to generate a new invite code for this event?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newInviteCode = generateUniqueInviteCode();
            eventToImport.setInviteCode(newInviteCode);
            var lostExpenses = clearIds(eventToImport);
            Event newEvent = server.addEvent(eventToImport);
            addExpenses(lostExpenses, newEvent);
            refresh();
            mainCtrl.showNotification("Event imported successfully", "#4CAF50");
        } else {
            mainCtrl.showNotification("Event import canceled", "#F44336");
        }
    }

    private List<Expense> clearIds(Event event) {
        event.setId(0);
        event.getParticipants().forEach(participant -> participant.setId(0));
        var lostExpenses = event.getExpenses();
        event.setExpenses(null);
        return lostExpenses;
    }

    private void addExpenses(List<Expense> expenses, Event newEvent){
        for (Expense expense: expenses){
            var participantsWithSameUsername = newEvent.getParticipants().stream().filter(p -> Objects.equals(p.getUserName(), expense.getCreator().getUserName())).toList();
            expense.setCreator(participantsWithSameUsername.getFirst());
            expense.setEvent(newEvent);
            server.addExpense(expense);
        }
    }

    private String generateUniqueInviteCode() {
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }

    public void refresh() {
        List<Event> events = server.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }

    private String formatDate(Timestamp date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ; hh:mm");
            return sdf.format(date);
        }
        return null;
    }

}
