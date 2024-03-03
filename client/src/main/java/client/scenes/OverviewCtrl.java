package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class OverviewCtrl {

    @FXML
    private Button sendInvitesButton, addParticipantButton, addExpenseButton, settleDebtsButton;

    @FXML
    private ComboBox<String> participantsComboBox;

    @FXML
    private ToggleButton allExpensesToggle, fromJohnToggle, includingJohnToggle;

    @FXML
    private TextField participantFirstNameField, participantLastNameField;

    @FXML
    private Text participantsText;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private TextArea expensesTextArea;

    private final Event event;

    public OverviewCtrl() {
        this.event = new Event("New Year Party");
    }

    @FXML
    public void initialize() {
        inviteCodeLabel.setText(event.getId() + "");
        updateParticipantsComboBox();
    }

    private void updateParticipantsComboBox() {
        participantsComboBox.getItems().clear();
        participantsComboBox.getItems().addAll(
                event.getParticipants().stream().map(Participant::getFirstName).toList()
        );
    }

    @FXML
    public void handleAddParticipantButton() {
        // Get the new participant's name and reset the fields
        String participantFirstName = participantFirstNameField.getText().trim();
        String participantLastName = participantLastNameField.getText().trim();
        participantFirstNameField.setText("");
        participantLastNameField.setText("");

        // Add the participant
        if (!participantFirstName.isEmpty() && !participantLastName.isEmpty()) {
            // Giving dummy details for participant
            Participant newParticipant = new Participant(participantFirstName, participantLastName, "email", "iban", "username");
            event.addParticipant(newParticipant);
            updateParticipantsComboBox();
            if (participantsText.getText() != null && !participantsText.getText().isEmpty()) {
                participantsText.setText(participantsText.getText() + ", " + newParticipant.getFirstName());
            } else {
                participantsText.setText(participantsText.getText() + newParticipant.getFirstName());
            }
            System.out.println("Participant added: " + newParticipant);
        }
    }

    @FXML
    public void handleSettleDebtsButton() {
        System.out.println("Settling debts among participants");
    }
}
