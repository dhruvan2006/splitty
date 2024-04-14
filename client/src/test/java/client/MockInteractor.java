package client;

import client.interactors.Interactor;

public class MockInteractor implements Interactor {
    @Override
    public String getText(Object o) {
        return null;
    }

    @Override
    public void clear(Object o) {

    }

    @Override
    public void setText(Object o, String s) {

    }

    @Override
    public <T> void select(Object o, T t) {

    }

    @Override
    public void createAlert(String s) {

    }
}
