package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.*;
import javafx.scene.control.Alert;

public class ExpensesCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

    public void add() {
        String title = description.getText();
        int value = Integer.parseInt(amount.getText());
        String creatorUsername = username.getText();

        if (title.isEmpty() || value<=0 || creatorUsername.isEmpty()) {
            showAlert("Please fill in all fields!");
            return;
        }

        Expense expense = new Expense(title, value, new Participant(null,null,creatorUsername));

        server.addExpense(expense);
        clearFields();
        showAlert("Expense added successfully!");
    }


    public List<Expense> getExpenses() {
        List<Expense> expenses = server.getExpenses();

        if (expenses == null || expenses.isEmpty()) {
            showAlert("No expenses found.");
        }

        return expenses;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expense Manager");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
