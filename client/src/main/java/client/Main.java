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

import client.scenes.OverviewCtrl;
import client.scenes.StartScreenCtrl;
import client.scenes.ParticipantCtrl;
import com.google.inject.Injector;

import client.scenes.MainCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }


    public void start(Stage primaryStage) throws IOException {

        var overview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var start = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, start, overview);
    }


//    @Override
//    public void start(Stage primaryStage) throws IOException {
//
//        var start = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
//        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
//        mainCtrl.initialize2(primaryStage, start);
//    }
}