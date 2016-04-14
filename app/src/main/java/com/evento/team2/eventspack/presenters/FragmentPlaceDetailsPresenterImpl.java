package com.evento.team2.eventspack.presenters;

import android.location.Location;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentPlaceDetailsPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;
import com.evento.team2.eventspack.views.FragmentPlaceDetailsView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class FragmentPlaceDetailsPresenterImpl implements FragmentPlaceDetailsPresenter {

    private FragmentPlaceDetailsView fragmentPlaceDetailsView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    public FragmentPlaceDetailsPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentPlaceDetailsView fragmentPlaceDetailsView) {
        this.fragmentPlaceDetailsView = fragmentPlaceDetailsView;
    }

    @Override
    public void fetchPlaceDetails(long placeId) {
        new Thread() {
            @Override
            public void run() {
                final Place place = databaseInteractor.getPlaceById(placeId);
                mainThread.post(() -> fragmentPlaceDetailsView.showPlace(place));
            }
        }.start();
    }

    @Override
    public void fetchActiveEventsByLocation(long placeId) {
        new Thread() {
            @Override
            public void run() {
                final Place place = databaseInteractor.getPlaceById(placeId);
                ArrayList<Event> eventArrayList = EventsDatabase.getInstance().getActiveEventsByLocation(String.valueOf(place.location.latitude),
                        String.valueOf(place.location.longitude), place.name, String.valueOf(new Date().getTime()));

                fragmentPlaceDetailsView.showEventsAtPlace(eventArrayList);
            }
        }.start();

    }
}