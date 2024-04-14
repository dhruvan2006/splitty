package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ExpensesCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Button finishButton;

    @FXML
    private ComboBox username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    private Event event;
    private boolean editMode;
    private Expense expense;
    private ResourceBundle bundle;


    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void initialize(Expense expense) {
        this.editMode = expense != null;
        username.getItems().clear();
        username.getItems().addAll(
                event.getParticipants().stream().map(Participant::getUserName).toList()
        );
        if (editMode){
            assert expense != null;
            setExpense(expense);
            initializeWithExpense(expense);
            finishButton.setText(bundle.getString("globals.edit"));
        }
        else {
            finishButton.setText(bundle.getString("globals.add"));
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        server.connectWebSocket();
        this.mainCtrl = mainCtrl;
        editMode = false;
        expense = null;
    }
    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    private void clearFields() {
        description.clear();
        amount.clear();
    }

    public void modify() {
        Expense modify = getExpenses();
        if (modify == null) return;
        if (!updateLastUsed()) return;
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
        mainCtrl.getOverviewCtrl().updateFinancialDashboard();
        mainCtrl.showOverview();
    }
    //TODO instead of retrieving all participants it is more efficient to just write a query, which I will do later
    public Expense getExpenses() {
        var participants = mainCtrl.getOverviewCtrl().getEvent().getParticipants();
        var filtered = participants.stream().filter(x -> Objects.equals(username.getSelectionModel().getSelectedItem(), x.getUserName()));
        var any = filtered.findAny();
        if(any.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(bundle.getString("expense.participant_not_found"));
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
            alert.setContentText(bundle.getString("expense.error_amount"));
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
                alert.setContentText(bundle.getString("expense.error_amount"));
                alert.showAndWait();
                return null;
            }
            if(unit >= 100) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(bundle.getString("expense.too_much_decimals"));
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
        username.getSelectionModel().select(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(expense.getTotalExpenseString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
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
}
