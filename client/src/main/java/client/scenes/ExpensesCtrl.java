package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class ExpensesCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private OverviewCtrl overviewCtrl;
    private Scene thisScene;
    private Event current;
    @FXML
    private TextField username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    public void setEvent(Event current){
        this.current = current;
    }

    public void setThisScene(Scene thisScene) {this.thisScene = thisScene;}

    public void setOverviewCtrl(OverviewCtrl overviewCtrl) {
        this.overviewCtrl = overviewCtrl;
    }

    @Inject
    public ExpensesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void cancel() {
        clearFields();
        back();
    }

    private void clearFields() {
        username.clear();
        description.clear();
        amount.clear();
    }
    public void add(){
        try {
            getExpenses();
            server.addExpense(getExpenses());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        back();
    }

    public Expense getExpenses() {
        Participant p = server.addParticipant(new Participant(username.getText(), username.getText(), username.getText(), current));
        return new Expense(description.getText(), Integer.parseInt(amount.getText()), p, current);
    }

    public void initializeWithExpense(Expense expense) {
        username.setText(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(Integer.toString(expense.getTotalExpense()));
    }
    public void show() {mainCtrl.showScene(thisScene, "Add Expense");}
    public void back() {overviewCtrl.show();}
}
