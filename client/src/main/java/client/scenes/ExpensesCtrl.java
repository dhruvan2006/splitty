package client.scenes;

import client.interactors.Interactor;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ExpensesCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Interactor expenseInteractor;
    @FXML
    public Button finishButton;

    @FXML
    private ComboBox<String> username;
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
        fillParticipantDropdown();
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

    public void fillParticipantDropdown(){
        username.getItems().addAll(
                event.getParticipants().stream().map(Participant::getUserName).toList()
        );
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl, Interactor expenseInteractor) {
        this.expenseInteractor = expenseInteractor;
        this.server = server;
        server.connectWebSocket();
        this.mainCtrl = mainCtrl;
        editMode = false;
        expense = null;
    }
    public void cancel() {
        clearFields();
        expense = null;
        mainCtrl.showOverview();
    }

    public void clearFields() {
        expenseInteractor.clear(description);
        expenseInteractor.clear(amount);
    }

    public boolean checkExpenseExistence(List<Expense> expenses) {
        // expense != null means we are editing a participant, not creating one
        if (expense != null && !expenses.contains(expense)) {
            cancel();
            return false;
        }
        return true;
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
        server.send("/app/websocket/notify/event", event);
        mainCtrl.getOverviewCtrl().updateExpenseList();
        mainCtrl.getOverviewCtrl().updateFinancialDashboard();
        mainCtrl.showOverview();
        expense = null;
    }
//    public void modify(String sUsername, String sAmount, String sDescription) {
//        Expense modify = getExpenses();
//        if (modify == null) return;
//        if (!updateLastUsed()) return;
//        System.out.println(modify.getCreator());
//        if (!editMode){
//            Expense added = server.addExpense(modify);
//            event.addExpense(added);
//        }
//        else {
//            event.getExpenses().remove(expense);
//            Expense updated = server.putExpense(expense.getId(), modify);
//            event.addExpense(updated);
//        }
//        clearFields();
//        mainCtrl.getOverviewCtrl().updateExpenseList();
//        mainCtrl.getOverviewCtrl().updateFinancialDashboard();
//        mainCtrl.showOverview();
//    }
    //TODO instead of retrieving all participants it is more efficient to just write a query, which I will do later
    public Expense getExpenses() {
        var participants = mainCtrl.getOverviewCtrl().getEvent().getParticipants();
        return getExpenses(participants, username.getSelectionModel().getSelectedItem(), amount.getText(), description.getText());
    }
    public Expense getExpenses(List<Participant> participants, String sUsername, String sAmount, String sDescription) {
        var filtered = participants.stream().filter(x -> Objects.equals(sUsername, x.getUserName()));
        var any = filtered.findAny();
        if(any.isEmpty()) {
            expenseInteractor.createAlert(bundle.getString("expense.participant_not_found"));
            return null;
        }
        int totalExpense;
        String[] twoParts = sAmount.split("\\.");
        if(twoParts.length > 2) {
            expenseInteractor.createAlert("At most one \".\"");
            return null;
        }
        String first = twoParts[0];
        int centum;
        try {
            centum = Integer.parseInt(first);
        }
        catch (Exception e) {
            expenseInteractor.createAlert(bundle.getString("expense.error_amount"));
            return null;
        }
        int unit = 0;
        if(twoParts.length > 1) {
            String second = twoParts[1];
            try {
                unit = Integer.parseInt(second);
            }
            catch (Exception e) {
                expenseInteractor.createAlert(bundle.getString("expense.error_amount"));
                return null;
            }
            if(unit >= 100) {
                expenseInteractor.createAlert(bundle.getString("expense.too_much_decimals"));
                return null;
            }
            if(second.length() == 1)
                unit*=10;
        }
        totalExpense = centum*100+unit;
        return new Expense(sDescription, totalExpense, any.get(), event);
    }

    public void initializeWithExpense(Expense expense) {
        expenseInteractor.select(username, expense.getCreator().getUserName());
        expenseInteractor.setText(description, expense.getTitle());
        expenseInteractor.setText(amount, expense.getTotalExpenseString());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public boolean updateLastUsed() {
        try{
            server.updateLastUsedDate(event.getId());
        }
        catch (WebApplicationException e){
            expenseInteractor.createAlert("Something went wrong");
            return false;
        }
        return true;
    }
}
