package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.Objects;

public class ExpensesCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    private Event event;
    private boolean edited;
    private Expense expense;

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        edited = false;
        expense = null;
    }
    public void cancel() {
        clearFields();
        mainCtrl.showOverviewWithEvent(event);
    }

    private void clearFields() {
        username.clear();
        description.clear();
        amount.clear();
    }
    public void modify() {
        Expense modify = getExpenses();
        if(expense == null){
            Expense added = server.addExpense(modify);
            event.addExpense(added);
        }
        else {
            event.getExpenses().remove(expense);
            Expense updated = server.putExpense(expense.getId(), modify);
            event.addExpense(updated);
        }
        mainCtrl.showOverviewWithEvent(event);
    }
    //TODO instead of retrieving all participants it is more efficient to just write a query, which I will do later
    public Expense getExpenses() {
        var participants = server.getParticipants();
        var filtered = participants.stream().filter(x -> Objects.equals(username.getText(), x.getUserName()));
        var any = filtered.findAny();
        if(any.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Participant does not exists");
            alert.showAndWait();
            return null;
        }
        int totalExpense;
        try {
            totalExpense = Integer.parseInt(amount.getText());
        }
        catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Have to be Integer");
            alert.showAndWait();
            return null;
        }
        Expense expense = new Expense(description.getText(), totalExpense, any.get(), event);
        clearFields();
        return expense;
    }

    public void initializeWithExpense(Expense expense) {
        username.setText(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(Integer.toString(expense.getTotalExpense()));
    }
}
