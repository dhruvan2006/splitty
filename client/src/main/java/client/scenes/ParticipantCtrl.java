package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private OverviewCtrl overviewCtrl;

    @FXML
    private Button finishButton;

    @FXML
    private TextField bnrField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    private final String borderColor = "-fx-border-color: rgb(182,180,180)";
    private ArrayList<TextField> fields;

    private Participant editingParticipant;

    @Inject
    public ParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        if (fields == null)
            fields = new ArrayList<>() {
            {
                add(bnrField);
                add(emailField);
                add(nameField);
            }
        };
        clearFields();
        editingParticipant = null;
        finishButton.setText("Add");
    }

    public void initializeWithParticipant(Participant participant) {
        this.editingParticipant = participant;
        finishButton.setText("Edit");
        if (participant != null) {
            bnrField.setText(participant.getIBAN());
            emailField.setText(participant.getEmail());
            nameField.setText(participant.getUserName());
        }
    }

    public void setOverviewCtrl(OverviewCtrl overviewCtrl) {
        this.overviewCtrl = overviewCtrl;
    }

    public void Cancel() {
        clearFields();
        System.out.println("Canceled creation of event");
        mainCtrl.showOverview();
    }

    private void clearFields(){
        bnrField.clear();
        emailField.clear();
        nameField.clear();
        for (TextField field : fields) {
            field.setStyle(borderColor);
        }
    }
    public boolean Validate() {
        boolean valid = true;
        for (TextField field : fields) {
            if (Objects.equals(field.getText(), "")){
                valid = false;
                field.setStyle("-fx-border-color: #E80C0C");
            }
            else {
                field.setStyle(borderColor);
            }
        }
        return valid;
    }

    public void handleFinishButton(){
        if(!Validate()){
            return;
        }
        if (editingParticipant != null) {
            editingParticipant.setIBAN(bnrField.getText());
            editingParticipant.setEmail(emailField.getText());
            editingParticipant.setUserName(nameField.getText());
            overviewCtrl.updateParticipant(editingParticipant);
        } else {
            Participant participant = new Participant(emailField.getText(), bnrField.getText(), nameField.getText());
            overviewCtrl.addParticipant(participant);
        }
        mainCtrl.showOverview();
    }
}
