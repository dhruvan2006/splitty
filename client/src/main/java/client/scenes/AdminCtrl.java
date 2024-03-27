package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
                        server.deleteEventById(eventToDelete.getId());
                        refresh();
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
                            saveJsonToFile(json, eventData.getTitle());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        refresh();
    }

    private void saveJsonToFile(String json, String eventTitle) throws IOException {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleImportEvent() {
        // TODO: Add the ability to import a JSON file
    }

    public void refresh() {
        List<Event> events = server.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }
}
