package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.Objects;

public class ExpensesCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Button finishButton;

    @FXML
    private TextField username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    private Event event;
    private boolean editMode;
    private Expense expense;

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void setEditMode(boolean editMode) {
        if (editMode){
            finishButton.setText("edit");
        }
        else {
            finishButton.setText("add");
        }
        this.editMode = editMode;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        editMode = false;
        expense = null;
    }
    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    private void clearFields() {
        username.clear();
        description.clear();
        amount.clear();
    }

    public void modify() {
        Expense modify = getExpenses();
        if (modify == null) return;
        System.out.println(modify.getCreator());
        if (!editMode){
            Expense added = server.addExpense(modify);
            event.addExpense(added);
        }
        else {
            event.getExpenses().remove(expense);
            Expense updated = server.putExpense(expense.getId(), modify);
            event.addExpense(updated);
        }
        clearFields();
        mainCtrl.getOverviewCtrl().updateExpenseList();
        mainCtrl.showOverview();
    }
    //TODO instead of retrieving all participants it is more efficient to just write a query, which I will do later
    public Expense getExpenses() {
        var participants = mainCtrl.getOverviewCtrl().getEvent().getParticipants();
        var filtered = participants.stream().filter(x -> Objects.equals(username.getText(), x.getUserName()));
        var any = filtered.findAny();
        if(any.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Participant does not exists in this event");
            alert.showAndWait();
            return null;
        }
        int totalExpense;
        String[] twoParts = amount.getText().split("\\.");
        if(twoParts.length > 2) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("At most one \".\"");
            alert.showAndWait();
            return null;
        }
        String first = twoParts[0];
        int centum;
        try {
            centum = Integer.parseInt(first);
        }
        catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Have to be Integer");
            alert.showAndWait();
            return null;
        }
        int unit = 0;
        if(twoParts.length > 1) {
            String second = twoParts[1];
            try {
                unit = Integer.parseInt(second);
            }
            catch (Exception e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Have to be Integer");
                alert.showAndWait();
                return null;
            }
            if(unit > 100) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Have to at most two digits after \".\"");
                alert.showAndWait();
                return null;
            }
            if(second.length() == 1)
                unit*=10;
        }
        totalExpense = centum*100+unit;
        return new Expense(description.getText(), totalExpense, any.get(), event);
    }

    public void initializeWithExpense(Expense expense) {
        username.setText(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(expense.getTotalExpenseString());
    }
}
