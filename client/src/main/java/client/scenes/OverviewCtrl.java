package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class OverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button sendInvitesButton, addParticipantButton, addExpenseButton, settleDebtsButton;

    @FXML
    private ComboBox<String> participantsComboBox;

    @FXML
    private ToggleButton allExpensesToggle, fromJohnToggle, includingJohnToggle;

    @FXML
    private TextField participantFirstNameField, participantLastNameField, participantEmailField, participantIBANField, participantUsernameField;

    @FXML
    private Text participantsText;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private TextArea expensesTextArea;

    private final Event event;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
//        TODO: Get the event from Start page
        this.event = new Event("New Year Party");
    }

    @FXML
    public void initialize() {
        inviteCodeLabel.setText(event.getInviteCode());
        updateParticipantsComboBox();
    }

    private void updateParticipantsComboBox() {
        participantsComboBox.getItems().clear();
        participantsComboBox.getItems().addAll(
                event.getParticipants().stream().map(Participant::getUserName).toList()
        );
    }

    @FXML
    public void handleAddParticipantButton() {
        // Get the new participant's name and reset the fields
        String participantFirstName = participantFirstNameField.getText().trim();
        String participantLastName = participantLastNameField.getText().trim();
        String participantEmail = participantEmailField.getText().trim();
        String participantIBAN = participantIBANField.getText().trim();
        String participantUsername = participantUsernameField.getText().trim();
        participantFirstNameField.setText("");
        participantLastNameField.setText("");
        participantEmailField.setText("");
        participantIBANField.setText("");
        participantUsernameField.setText("");

        // Add the participant
        if (!participantFirstName.isEmpty() && !participantLastName.isEmpty() && !participantEmail.isEmpty() && !participantIBAN.isEmpty() && !participantUsername.isEmpty()) {
            System.out.println(participantFirstName);
            System.out.println(participantLastName);
            System.out.println(participantEmail);
            System.out.println(participantIBAN);
            System.out.println(participantUsername);
            Participant newParticipant = new Participant(participantEmail,
                    participantIBAN, participantUsername, event);

            server.addParticipant(newParticipant);

            event.addParticipant(newParticipant);
            updateParticipantsComboBox();
            if (participantsText.getText() != null && !participantsText.getText().isEmpty()) {
                participantsText.setText(participantsText.getText() + ", " + newParticipant.getUserName());
            } else {
                participantsText.setText(participantsText.getText() + newParticipant.getUserName());
            }
            System.out.println("Participant added: " + newParticipant);
        }
    }

    @FXML
    public void handleSettleDebtsButton() {
        System.out.println("Settling debts among participants");
    }
}
