package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import commons.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Event current;

    @FXML
    private TextField bnrField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    private ArrayList<TextField> fields;

    @Inject
    public ParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
         fields = new ArrayList<>() {
            {
                add(bnrField);
                add(emailField);
                add(nameField);
            }
        };
    }

    public void Cancel() {
        clearFields();
        System.out.println("Canceled creation of event");
        mainCtrl.showStartScreen();
    }

    private void clearFields(){
        bnrField.clear();
        emailField.clear();
        nameField.clear();
    }
    public boolean Validate() {
        boolean valid = true;
        for (TextField field : fields) {
            if (Objects.equals(field.getText(), "")){
                valid = false;
                field.setStyle("-fx-border-color: #E80C0C");
            }
            else {
                field.setStyle("-fx-border-color: gray");
            }
        }
        return valid;
    }
    public void Finish(){
        if(!Validate()){
            return;
        }
        mainCtrl.showOverview();
    }


    public Event getCurrent() {
        return current;
    }

    public void setCurrent(Event current) {
        this.current = current;
    }
}
