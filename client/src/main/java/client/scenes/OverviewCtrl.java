package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class OverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Event current;

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

    private ExpensesCtrl expensesCtrl;
    private Scene expense;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void initialize(Pair<ExpensesCtrl, Parent> pe) {
        this.expensesCtrl = pe.getKey();
        this.expense = new Scene(pe.getValue());
    }

    @FXML
    public void initialize() {
        if(current == null){
            return;
        }
        inviteCodeLabel.setText(current.getInviteCode());
        updateParticipantsComboBox();
    }

    private void updateParticipantsComboBox() {
        participantsComboBox.getItems().clear();
        participantsComboBox.getItems().addAll(
                current.getParticipants().stream().map(Participant::getUserName).toList()
        );
    }

    @FXML
    private void  addExpense() {
        mainCtrl.showScene(expense, "Expenses");
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
                    participantIBAN, participantUsername, current);

            server.addParticipant(newParticipant);

            current.addParticipant(newParticipant);
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


    public Event getCurrent() {
        return current;
    }

    public void setCurrent(Event current) {
        this.current = current;
    }
}
