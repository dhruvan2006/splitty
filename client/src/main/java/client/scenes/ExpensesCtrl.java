package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

public class ExpensesCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
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
    //to be done
    public void add(){
        server.addQuote(new Quote(new Person("fw", "j"), "huj v ,muke"));
        Event ev =  server.addEvent(new Event("v rot jebus"));
        server.addParticipant(new Participant("dg", "df", "df", ev));
        //server.addExpense(getExpenses());
    }

    public Expense getExpenses() {
        return new Expense("huj", 9, new Participant("rf", "er", "erg", new Event("gg")), new Event("gg"));
    }

    public void initializeWithExpense(Expense expense) {
        username.setText(expense.getCreator().getUserName());
        description.setText(expense.getTitle());
        amount.setText(Integer.toString(expense.getTotalExpense()));
    }
}
