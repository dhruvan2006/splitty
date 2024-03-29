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
import javafx.scene.input.MouseEvent;
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
    private Label titleLabel, inviteCodeLabel;

    private ExpensesCtrl expensesCtrl;
    private Scene expenseScene;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(Pair<ExpensesCtrl, Parent> pe) {
        this.expensesCtrl = pe.getKey();
        this.expenseScene = new Scene(pe.getValue());
    }

    @FXML
    public void initialize() {
        if(event == null){
            return;
        }
        titleTextField = new TextField();
        titleTextField.setPromptText("Enter title here...");

        titleLabel.setText(event.getTitle());
        inviteCodeLabel.setText(event.getInviteCode());
        updateParticipantsComboBox();
        updateParticipantsList();
//        event.setExpenses(new ArrayList<>() {
//            {
//                for (int i = 0; i < 10; i ++) {
//                    add(new Expense("title", 11, new Participant("hi@hi.com", "iban", "janpietklaas")));
//                    add(new Expense("title2", 22, new Participant("hi2@hi2.com", "iban2", "klaasjanpiet")));
//                }
//            };
//        }); // TODO: Remove this mock data when you can add expenses via the expense scene
        updateExpenseList();
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
            Button editButton = new Button("Edit");
            Button removeButton = new Button("Remove");

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
        this.event = server.addParticipantToEvent(event.getId(), participant);
        updateParticipantsList();
        updateParticipantsComboBox();
    }

    public void updateParticipant(Participant updatedParticipant) {
        this.event = server.updateParticipantInEvent(event.getId(), updatedParticipant);
        updateParticipantsList();
        updateParticipantsComboBox();
    }

    private void editParticipant(Participant participant) {
        ParticipantCtrl participantCtrl = mainCtrl.getParticipantCtrl();
        participantCtrl.initializeWithParticipant(participant);
        mainCtrl.showConfigParticipant(this);
    }

    private void removeParticipant(Participant participant) {
        this.event = server.removeParticipantFromEvent(event.getId(), participant.getId());
        updateParticipantsList();
        updateParticipantsComboBox();
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
            Text amount = new Text("\u20ac" + expense.getTotalExpense()*1.0/100);
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
        expensesCtrl.setExpense(expense);
        expensesCtrl.initializeWithExpense(expense);
        mainCtrl.showScene(expenseScene, "Edit Expense");
    }

    private void deleteExpense(Expense expense) {
        event.getExpenses().remove(expense);
        server.deleteExpense(expense.getId());
        updateExpenseList();
    }

    @FXML
    private void addExpense() {
        expensesCtrl.setEvent(event);
        mainCtrl.showScene(expenseScene, "Expenses");
    }

    @FXML
    public void handleAddParticipantButton() {
        mainCtrl.getParticipantCtrl().setEvent(event);
        mainCtrl.getParticipantCtrl().initialize();
        mainCtrl.showConfigParticipant(this);
    }

    @FXML
    public void handleSettleDebtsButton() {
        System.out.println("Settling debts among participants");
    }

    @FXML
    public void handleTitleButton() {
        if (!titleHBox.getChildren().contains(titleTextField)) {
            titleHBox.getChildren().addFirst(titleTextField);
            titleTextField.setText(titleLabel.getText());
            titleHBox.getChildren().remove(titleLabel);
            titleButton.setText("Apply Changes");
        } else {
            System.out.println(event.getId());
            this.event = server.updateEventTitle(event.getId(), titleTextField.getText());
            titleLabel.setText(event.getTitle());
            titleHBox.getChildren().remove(titleTextField);
            titleHBox.getChildren().addFirst(titleLabel);
            titleButton.setText("Change Title");
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void back(MouseEvent mouseEvent) {
        mainCtrl.showStartScreen();
    }
}
