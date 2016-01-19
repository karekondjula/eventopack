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
    public void fetchEvents() {
        new Thread() {
            @Override
            public void run() {

                String lastUpdateDate = preferencesInteractor.getLastUpdateOfEvents();
                Date today = new Date();
                String todayDate = DateFormatterUtils.compareDateFormat.format(today);

                if (!todayDate.equals(lastUpdateDate)) {
                    // get new events from server
                    fetchEventsFromServer();
                    preferencesInteractor.setLastUpdateOfEvents(todayDate);
                }

                final ArrayList<Event> eventArrayList = EventsDatabase.getInstance().getEvents(FetchAsyncTask.NO_FILTER_STRING, String.valueOf(new Date().getTime()));

                mainThread.post(() -> {
                    if (eventArrayList.size() != 0) {
                        fragmentEventsView.hideNoEventsView();
                        fragmentEventsView.showEvents(eventArrayList);
                    } else {
                        fragmentEventsView.showNoEventsView();
                    }
                });
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

    @Override
    public void fetchEventsFromServer() {
        new Thread() {
            @Override
            public void run() {
                if (NetworkUtils.getInstance().isNetworkAvailable(EventiApplication.applicationContext)) {
                    HashMap<String, Object> params = new HashMap();
                    params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
                    ServiceEvento.getInstance().callServiceMethod(params);

                    Date today = new Date();
                    String todayDate = DateFormatterUtils.fullDateFormat.format(today);
                    preferencesInteractor.setLastUpdateOfEvents(todayDate);

                    filterEvents(lastQuery);
                } else {
                    mainThread.post(fragmentEventsView::showNoInternetConnectionMessage);
                }
            }
        }.start();
    }

    @Override
    public void fetchLastUpdatedTimestamp() {
//        String lastUpdateDate = "NOW";
        // TODO make it more human friendly
        String lastUpdateDate = preferencesInteractor.getLastUpdateOfEvents();

        fragmentEventsView.showLastUpdatedTimestampMessage(lastUpdateDate);
    }
}
