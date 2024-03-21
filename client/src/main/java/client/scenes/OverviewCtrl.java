package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

public class OverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public VBox expenseListVBox;

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

    private final Event event;

    private ExpensesCtrl expensesCtrl;
    private Scene expenseScene;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        //TODO: Get the event from Start page
        this.event = new Event("New Year Party");
    }

    public void initialize(Pair<ExpensesCtrl, Parent> pe) {
        this.expensesCtrl = pe.getKey();
        this.expenseScene = new Scene(pe.getValue());
    }

    @FXML
    public void initialize() {
        inviteCodeLabel.setText(event.getInviteCode());
        updateParticipantsComboBox();
        updateExpenseList();
    }

    private void updateParticipantsComboBox() {
        participantsComboBox.getItems().clear();
        participantsComboBox.getItems().addAll(
                event.getParticipants().stream().map(Participant::getUserName).toList()
        );
    }

    private void updateExpenseList() {
        expenseListVBox.getChildren().clear();

        if (event.getExpenses().isEmpty()) {
            Label label = new Label("No expenses yet");
            label.setStyle("-fx-font-size: 20");
            expenseListVBox.getChildren().add(label);
        }

        for (Expense expense : event.getExpenses()) {
            TextFlow flow = new TextFlow();

            Text payer = new Text(expense.getCreator().getUserName());
            payer.setStyle("-fx-font-weight: bold");
            Text textPaid = new Text(" paid ");
            Text amount = new Text("\u20ac" + expense.getTotalExpense());
            amount.setStyle("-fx-font-weight: bold");
            Text textFor = new Text(" for ");
            Text title = new Text(expense.getTitle());
            flow.setStyle("-fx-font-size: 20");
            flow.getChildren().addAll(payer, textPaid, amount, textFor, title);
            title.setStyle("-fx-font-weight: bold");

            final Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            spacer.setMinSize(20, 1);

            HBox expenseHBox = new HBox(10);
            HBox.setMargin(flow, new Insets(0, 0, 0, 10));
            Button editButton = new Button("Edit");
            editButton.setAlignment(Pos.BASELINE_RIGHT);
            Button deleteButton = new Button("Delete");

            editButton.setOnAction(e -> editExpense(expense));
            deleteButton.setOnAction(e -> deleteExpense(expense));

            expenseHBox.getChildren().addAll(flow, spacer, editButton, deleteButton);
            expenseListVBox.getChildren().add(expenseHBox);
        }
    }

    private void editExpense(Expense expense) {
        expensesCtrl.initializeWithExpense(expense);
        mainCtrl.showScene(expenseScene, "Edit Expense");
    }

    private void deleteExpense(Expense expense) {
        event.getExpenses().remove(expense);
        updateExpenseList();
    }


    @FXML
    private void addExpense() {
        mainCtrl.showScene(expenseScene, "Expenses");
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
