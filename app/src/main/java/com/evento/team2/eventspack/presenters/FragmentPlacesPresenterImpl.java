package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlacesPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentPlacesView;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentPlacesPresenterImpl implements FragmentPlacesPresenter {

    private FragmentPlacesView fragmentPlacesView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    private String lastQuery = "";

    public FragmentPlacesPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentPlacesView fragmentEventsView) {
        this.fragmentPlacesView = fragmentEventsView;
    }

    @Override
    public void fetchPlaces(String query) {
        new Thread() {
            @Override
            public void run() {

                final ArrayList<Place> placeArrayList = databaseInteractor.getPlaces(lastQuery);
                mainThread.post(() -> fragmentPlacesView.showPlaces(placeArrayList));
            }
        }.start();

        lastQuery = query;
    }
}