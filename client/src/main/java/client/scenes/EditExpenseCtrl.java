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

public class EditExpenseCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private long id;
    @FXML
    private TextField username;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    @Inject
    public EditExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
    //to be done
    public void update(){
        server.updateExpense(getExpense(), id);
    }
    public Expense getExpense() {
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
        //Current Event is implemented in another branch so now I am replacing it with mock
        Event added = server.addEvent(new Event("huj"));
        Expense expense = new Expense(description.getText(), totalExpense, any.get(), added);
        clearFields();
        return expense;
    }

    public void initializeWithExpense(Expense expense) {
        username.setText(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(Integer.toString(expense.getTotalExpense()));
    }

    public void setId(long id) {
        this.id = id;
    }
}
