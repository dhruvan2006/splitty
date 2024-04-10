/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main2 extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private static boolean showAdminPage = false;

    public static void main(String[] args) throws URISyntaxException, IOException {
        for (String arg : args) {
            if ("--admin".equals(arg)) {
                showAdminPage = true;
                break;
            }
        }

        launch(args);
    }


    public void start(Stage primaryStage) throws IOException {
        var expense = FXML.load(ExpensesCtrl.class, "client", "scenes", "Expenses.fxml");
        var overview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var start = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var cParticipant = FXML.load(ParticipantCtrl.class, "client", "scenes", "Participant.fxml");
        var admin = FXML.load(AdminCtrl.class, "client", "scenes", "Admin.fxml");
        var joinAdmin = FXML.load(JoinAdminCtrl.class, "client", "scenes", "JoinAdmin.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        overview.getKey().initialize(expense);
        mainCtrl.initialize(primaryStage, start, cParticipant, overview, admin, joinAdmin);

        if (showAdminPage) {
            mainCtrl.showJoinAdmin();
        }
    }


//    @Override
//    public void start(Stage primaryStage) throws IOException {
//
//        var start = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
//        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
//        mainCtrl.initialize2(primaryStage, start);
//    }
}
