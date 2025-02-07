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


import client.interactors.DefaultInteractor;
import client.interactors.Interactor;
import client.scenes.OverviewCtrl;
import client.scenes.ParticipantCtrl;
import client.scenes.StartScreenCtrl;

import client.utils.ServerUtils;
import client.utils.ServerUtilsInterface;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import client.scenes.MainCtrl;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ParticipantCtrl.class).in(Scopes.SINGLETON);
    }
    @Provides
    public ServerUtilsInterface providesServerUtils() {return new ServerUtils();}
    @Provides
    public Interactor provideExpenseInteractor() {return new DefaultInteractor();}
}