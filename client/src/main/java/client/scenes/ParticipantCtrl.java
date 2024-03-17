package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Objects;

public class ParticipantCtrl {
    @FXML
    private TextField bnrField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    private ArrayList<TextField> fields;
    @FXML
    public void initialize() {
         fields = new ArrayList<>() {
            {
                add(bnrField);
                add(emailField);
                add(nameField);
            }
        };
    }
    @FXML
    private void Cancel() {
        System.out.println("Configuring Participant Canceled");
    }
    @FXML
    private void HandleFinish(){
        if (!Validate()){
            return;
        }
        Finish();
    }
    private boolean Validate() {
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
    private void Finish(){
        System.out.println("Configuring Participant Finished");
    }
}
