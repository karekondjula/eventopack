package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.presenters.interfaces.FragmentSavedEventsPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.ui.fragments.FragmentSavedEvents;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.NetworkUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventsView;
import com.evento.team2.eventspack.views.FragmentSavedEventsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
