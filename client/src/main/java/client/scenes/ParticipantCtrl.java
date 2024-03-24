package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Scene thisScene;
    private OverviewCtrl overviewCtrl;

    private OverviewCtrl overviewCtrl;

    @FXML
    private Button finishButton;

    @FXML
    private TextField bnrField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    private ArrayList<TextField> fields;
    public void setThisScene(Scene thisScene) {
        this.thisScene = thisScene;
    }

    public void setOverviewCtrl(OverviewCtrl overviewCtrl) {
        this.overviewCtrl = overviewCtrl;
    }
    private Participant editingParticipant;

    @Inject
    public ParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        clearFields();
        editingParticipant = null;
        finishButton.setText("Add");
         fields = new ArrayList<>() {
            {
                add(bnrField);
                add(emailField);
                add(nameField);
            }
        };
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
        overviewCtrl.show();
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
                field.setStyle("-fx-border-color: grey");
            }
        }
        return valid;
    }

    public void handleFinishButton(){
        if(!Validate()){
            return;
        }
        overviewCtrl.show();
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
>>>>>>> client/src/main/java/client/scenes/ParticipantCtrl.java
    }

}
