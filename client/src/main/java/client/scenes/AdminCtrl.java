package client.scenes;

import client.utils.ServerUtils;
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
import javafx.util.Callback;

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

        deleteColumn.setCellFactory(createDeleteButtonCell());
        jsonColumn.setCellFactory(createJsonButtonCell());

        refresh();
    }

    private Callback<TableColumn<Event, String>, TableCell<Event, String>> createDeleteButtonCell() {
        return param -> new TableCell<>() {
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
                        // Implement deletion logic here, e.g., server.deleteEvent(eventToDelete.getId());
                        refresh(); // Refresh the table view
                    });
                }
            }
        };
    }

    private Callback<TableColumn<Event, String>, TableCell<Event, String>> createJsonButtonCell() {
        return param -> new TableCell<>() {
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
                        // Implement logic to show JSON, e.g., showDialogWithJson(eventData);
                    });
                }
            }
        };
    }

    public void handleImportEvent() {
        return;
    }

    public void refresh() {
        List<Event> events = server.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }
}
