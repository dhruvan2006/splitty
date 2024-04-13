package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.util.Pair;

import java.net.URL;
import java.util.Map;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    boolean isEditingTitle = false;

    private Event event;

    @FXML
    public VBox expenseListVBox, participantsVBox;

    @FXML
    public HBox titleHBox;

    @FXML
    private Button titleButton, sendInvitesButton, addParticipantButton, addExpenseButton, settleDebtsButton;

    @FXML
    private ComboBox<String> participantsComboBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private Label titleLabel, inviteCodeLabel, totalExpensesLabel, sharePerPersonLabel;

    @FXML
    private ListView<String> debtsListView;


    private ExpensesCtrl expensesCtrl;
    private Scene expenseScene;
    private ResourceBundle bundle;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        server.registerForMessages("/topic/event", Event.class, event1 -> {
            System.out.println("received");
            Platform.runLater(() -> {
                if(event1.getId() == event.getId())
                    mainCtrl.showOverviewWithEvent(event1);
            });
        });
    }

    public void initialize(Pair<ExpensesCtrl, Parent> pe) {
        this.expensesCtrl = pe.getKey();
        this.expenseScene = new Scene(pe.getValue());
    }

    public void initialize() {
        if(event == null){
            return;
        }

        titleTextField = new TextField();
        titleTextField.setPromptText("Enter title here...");

        titleLabel.setText(event.getTitle());
        inviteCodeLabel.setText(event.getInviteCode());
        updateParticipantsList();
        updateParticipantsComboBox();
        updateExpenseList();
        updateFinancialDashboard();
    }

    private void updateParticipantsComboBox() {
        participantsComboBox.getItems().clear();
        participantsComboBox.getItems().addAll(
                event.getParticipants().stream().map(Participant::getUserName).toList()
        );
    }

    private void updateParticipantsList() {
        if (event == null || participantsVBox == null) {
            return;
        }

        participantsVBox.getChildren().clear();

        for (Participant participant : event.getParticipants()) {
            Label nameLabel = new Label(participant.getUserName());
            Button editButton = new Button(bundle.getString("globals.edit"));
            Button removeButton = new Button(bundle.getString("globals.remove"));

            editButton.setOnAction(e -> editParticipant(participant));
            removeButton.setOnAction(e -> removeParticipant(participant));

            final Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            spacer.setMinSize(20, 1);

            HBox participantHBox = new HBox(10, nameLabel, spacer, editButton, removeButton);
            participantHBox.setAlignment(Pos.CENTER_LEFT);

            participantsVBox.getChildren().add(participantHBox);
        }
    }

    public void addParticipant(Participant participant) {
        if (!updateLastUsed()) return;
        this.event = server.addParticipantToEvent(event.getId(), participant);
        updateParticipantsList();
        updateParticipantsComboBox();
        updateFinancialDashboard();
        server.send("/app/websocket/notify/event", event);
    }

    public void updateParticipant(Participant updatedParticipant) {
        if (!updateLastUsed()) return;
        this.event = server.updateParticipantInEvent(event.getId(), updatedParticipant);
        updateParticipantsList();
        updateParticipantsComboBox();
        updateFinancialDashboard();
        updateExpenseList();
        server.send("/app/websocket/notify/event", event);
    }

    private void editParticipant(Participant participant) {
        if (!updateLastUsed()) return;
        ParticipantCtrl participantCtrl = mainCtrl.getParticipantCtrl();
        participantCtrl.initializeWithParticipant(participant);
        mainCtrl.showConfigParticipant(this);
        updateFinancialDashboard();
    }

    private void removeParticipant(Participant participant) {
        if (!updateLastUsed()) return;
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(bundle.getString("overview.remove_participant_alert.title"));
        alert.setHeaderText(String.format(bundle.getString("overview.remove_participant_alert.header"), participant.userName));
        alert.setContentText(bundle.getString("overview.remove_participant_alert.content"));
        ButtonType ok = new ButtonType(bundle.getString("globals.remove"));
        ButtonType cancel = new ButtonType(bundle.getString("globals.cancel"));
        alert.getDialogPane().getButtonTypes().clear();
        alert.getDialogPane().getButtonTypes().add(ok);
        alert.getDialogPane().getButtonTypes().add(cancel);
        alert.showAndWait().ifPresent(response -> {
            if (response == ok){
                this.event = server.removeParticipantFromEvent(event.getId(), participant.getId());
                server.send("/app/websocket/notify/event", event);
                ArrayList<Expense> toDelete = new ArrayList<Expense>();
                for (Expense expense: this.event.getExpenses()){
                    if (expense.getCreator().getId() == participant.getId()){
                        server.deleteExpense(expense.getId());
                        toDelete.add(expense);
                    }
                }
                for (Expense expense: toDelete){
                    this.event.getExpenses().remove(expense);
                }
                updateParticipantsList();
                updateParticipantsComboBox();
                updateExpenseList();
                updateFinancialDashboard();
            }
        });
    }

    public void updateExpenseList() {
        expenseListVBox.getChildren().clear();

        if (event.getExpenses().isEmpty()) {
            Label label = new Label(bundle.getString("overview.no_expenses"));
            label.setStyle("-fx-font-size: 20");
            expenseListVBox.getChildren().add(label);
        }

        for (Expense expense : event.getExpenses()) {
            TextFlow flow = new TextFlow();

            Text payer = new Text(expense.getCreator().getUserName());
            payer.setStyle("-fx-font-weight: bold");
            Text textPaid = new Text(" " + bundle.getString("overview.paid") + " ");
            Text amount = new Text("\u20ac" + expense.getTotalExpenseString());
            amount.setStyle("-fx-font-weight: bold");
            Text textFor = new Text(" " + bundle.getString("overview.for") + " ");
            Text title = new Text(expense.getTitle());
            flow.setStyle("-fx-font-size: 20");
            flow.getChildren().addAll(payer, textPaid, amount, textFor, title);
            title.setStyle("-fx-font-weight: bold");

            final Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            spacer.setMinSize(20, 1);

            HBox expenseHBox = new HBox(10);
            HBox.setMargin(flow, new Insets(0, 0, 0, 10));
            Button editButton = new Button(bundle.getString("globals.edit"));
            editButton.setAlignment(Pos.BASELINE_RIGHT);
            Button deleteButton = new Button(bundle.getString("globals.remove"));

            editButton.setOnAction(e -> editExpense(expense));
            deleteButton.setOnAction(e -> deleteExpense(expense));

            expenseHBox.getChildren().addAll(flow, spacer, editButton, deleteButton);
            expenseListVBox.getChildren().add(expenseHBox);
        }
    }

    private void editExpense(Expense expense) {
        if (!updateLastUsed()) return;
        expensesCtrl.setEvent(event);
        updateFinancialDashboard();
        expensesCtrl.initialize(expense);
        mainCtrl.showScene(expenseScene, "Edit Expense");
    }

    private void deleteExpense(Expense expense) {
        if (!updateLastUsed()) return;
        event.getExpenses().remove(expense);
        server.deleteExpense(expense.getId());
        updateExpenseList();
        updateFinancialDashboard();
    }

    @FXML
    private void addExpense() {
        if (!updateLastUsed()) return;
        expensesCtrl.setEvent(event);
        expensesCtrl.initialize(null);
        mainCtrl.showScene(expenseScene, "Expenses");
        updateFinancialDashboard();
    }

    @FXML
    public void handleAddParticipantButton() {
        if (!updateLastUsed()) return;
        mainCtrl.getParticipantCtrl().initializeWithParticipant(null);
        mainCtrl.showConfigParticipant(this);
    }

    @FXML
    public void handleSettleDebtsButton() {
        if (!updateLastUsed()) return;
        System.out.println("Settling debts among participants");
    }

    @FXML
    public void handleTitleButton() {
        if (!updateLastUsed()) return;
        isEditingTitle = !isEditingTitle;
        if (!titleHBox.getChildren().contains(titleTextField)) {
            titleHBox.getChildren().addFirst(titleTextField);
            titleTextField.setText(titleLabel.getText());
            titleHBox.getChildren().remove(titleLabel);
            titleButton.setText(bundle.getString("overview.apply_changes"));
        } else {
            System.out.println(event.getId());
            this.event = server.updateEventTitle(event.getId(), titleTextField.getText());
            titleLabel.setText(event.getTitle());
            titleHBox.getChildren().remove(titleTextField);
            titleHBox.getChildren().addFirst(titleLabel);
            titleButton.setText(bundle.getString("overview.change_title"));
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void back(MouseEvent mouseEvent) {
        if (isEditingTitle) handleTitleButton();
        mainCtrl.showStartScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        this.bundle = resources;
    }

    private boolean updateLastUsed() {
        try{
            server.updateLastUsedDate(event.getId());
        }
        catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void updateFinancialDashboard() {
        System.out.println("Event's expenses: " + event.getExpenses());
        double totalExpenses = event.getExpenses().stream().mapToDouble(Expense::getTotalExpense).sum();
        totalExpensesLabel.setText(String.format(java.util.Locale.US, "\u20AC%.2f", totalExpenses / 100f));

        int participantCount = event.getParticipants().size();
        double sharePerPerson = totalExpenses / participantCount;
        sharePerPersonLabel.setText(String.format(java.util.Locale.US, "\u20AC%.2f", sharePerPerson / 100f));
        debtsListView.getItems().clear();

        Map<Participant, Map<Participant, Integer>> optimizedDebts = event.calculateOptimizedDebts();

        // Display the individual debts
        for (Map.Entry<Participant, Map<Participant, Integer>> entry : optimizedDebts.entrySet()) {
            Participant participant = entry.getKey();
            Map<Participant, Integer> debts = entry.getValue();
            for (Map.Entry<Participant, Integer> debtEntry : debts.entrySet()) {
                String debtInfo = String.format(bundle.getString("overview.owes") + ": \u20AC%.2f", participant.getUserName(), debtEntry.getKey().getUserName(), debtEntry.getValue() / 100f);
                debtsListView.getItems().add(debtInfo);
            }
        }

        if (debtsListView.getItems().isEmpty()) {
            Label noDebtsLabel = new Label(bundle.getString("overview.no_outstanding_debts"));
            noDebtsLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");
            noDebtsLabel.setAlignment(Pos.CENTER);

            StackPane placeholderPane = new StackPane(noDebtsLabel);
            placeholderPane.setAlignment(Pos.CENTER);

            debtsListView.setPlaceholder(placeholderPane);
        } else {
            debtsListView.setPlaceholder(new Label(""));
        }
    }
}
