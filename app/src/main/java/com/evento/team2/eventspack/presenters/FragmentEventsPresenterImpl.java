package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.PreferencesInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.ui.fragments.FragmentEvents;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.NetworkUtils;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentEventsPresenterImpl implements FragmentEventsPresenter {

    private EventiApplication application;
    private FragmentEventsView fragmentEventsView;
    private MainThread mainThread;
    private String lastQuery = "";

    PreferencesInteractor preferencesInteractor;

    public FragmentEventsPresenterImpl(EventiApplication application, PreferencesInteractor preferencesInteractor,
                                       MainThread mainThread) {
        this.application = application;
        this.preferencesInteractor = preferencesInteractor;
        this.mainThread = mainThread;
    }

    @Override
    public void setView(FragmentEvents fragmentEventsView) {
        this.fragmentEventsView = fragmentEventsView;
    }

    @Override
    public void fetchEvents(boolean forceUpdate) {
        new Thread() {
            @Override
            public void run() {

                long lastUpdateDate = preferencesInteractor.getLastUpdateOfEvents();
                Calendar today, lastUpdateOfEvents;
                today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                lastUpdateOfEvents = Calendar.getInstance();
                lastUpdateOfEvents.setTimeInMillis(lastUpdateDate);
                lastUpdateOfEvents.set(Calendar.HOUR_OF_DAY, 0);
                lastUpdateOfEvents.set(Calendar.MINUTE, 0);
                lastUpdateOfEvents.set(Calendar.SECOND, 0);
                lastUpdateOfEvents.set(Calendar.MILLISECOND, 0);

                if (forceUpdate || today.getTimeInMillis() != lastUpdateOfEvents.getTimeInMillis()) {
                    // get new events from server
                    fetchEventsFromServer();
                }

                final ArrayList<Event> eventArrayList = EventsDatabase.getInstance().getEvents(lastQuery, String.valueOf(new Date().getTime()));

                mainThread.post(() -> fragmentEventsView.showEvents(eventArrayList));
            }
        }.start();
    }

    @Override
    public void filterEvents(String query) {
        new Thread() {
            @Override
            public void run() {
                final ArrayList<Event> eventArrayList = EventsDatabase.getInstance().getEvents(query, String.valueOf(new Date().getTime()));

                mainThread.post(() -> fragmentEventsView.showEvents(eventArrayList));
            }
        }.start();
        lastQuery = query;
    }

    private void fetchEventsFromServer() {
        if (NetworkUtils.getInstance().isNetworkAvailable(EventiApplication.applicationContext)) {

            mainThread.post(fragmentEventsView::startRefreshAnimation);

            HashMap<String, Object> params = new HashMap();
            params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
            ServiceEvento.getInstance().callServiceMethod(params);

            preferencesInteractor.setLastUpdateOfEvents(new Date().getTime());

            mainThread.post(fragmentEventsView::stopRefreshAnimation);
        } else {
            mainThread.post(fragmentEventsView::showNoInternetConnectionMessage);
        }
    }

    @Override
    public void fetchLastUpdatedTimestamp() {
//        String lastUpdateDate = "NOW";
        // TODO make it more human friendly
        String lastUpdateDate = DateFormatterUtils.fullDateFormat.format(preferencesInteractor.getLastUpdateOfEvents());

        fragmentEventsView.showLastUpdatedTimestampMessage(lastUpdateDate);
    }
}
