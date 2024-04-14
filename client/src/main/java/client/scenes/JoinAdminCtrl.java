package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class JoinAdminCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField password;

    @Inject
    public JoinAdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        server.connectWebSocket();
        this.mainCtrl = mainCtrl;
    }

    public void join(){
        String x = password.getText();
        if(server.authenticateAdmin(x)){
            password.clear();
            mainCtrl.showAdminScreen();
        }
        else{
            mainCtrl.showNotification("Wrong Password", "#FF6666", 2);
            password.setStyle("-fx-border-color: #FF6666");
        }
    }
}