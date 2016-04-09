package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentSavedEventsPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentSavedEventsView;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentSavedEventsPresenterImpl implements FragmentSavedEventsPresenter {

    private FragmentSavedEventsView fragmentSavedEventsView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    public FragmentSavedEventsPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentSavedEventsView fragmentSavedEventsView) {
        this.fragmentSavedEventsView = fragmentSavedEventsView;
    }

    @Override
    public void fetchSavedEvents(String query) {
        new Thread() {
            @Override
            public void run() {

                ArrayList<Event> savedEventsArrayList = databaseInteractor.getSavedEvents(query);
                mainThread.post(() -> fragmentSavedEventsView.showSavedEvents(savedEventsArrayList));
            }
        }.start();
    }
}
