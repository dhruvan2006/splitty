package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private OverviewCtrl overviewCtrl;

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

    public void handleFinishButton(){
        if(!Validate()){
            return;
        }
        Participant newParticipant = new Participant(emailField.getText(), bnrField.getText(), nameField.getText());
        overviewCtrl.addParticipant(newParticipant);
        mainCtrl.showOverview();
    }


}
