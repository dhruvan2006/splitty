package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
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
        double amount = Double.parseDouble(amount.getText());
        String creatorName = username.getText();

        if (title.isEmpty() || amount.getText().isEmpty() || creatorName.isEmpty()) {
            showAlert("Please fill in all fields!");
            return;
        }

        Expense expense = new Expense(title, amount, creatorName);

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
