package com.evento.team2.eventspack.provider;

import android.os.AsyncTask;
import android.support.annotation.IntDef;

import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class FetchAsyncTask extends AsyncTask<String, Void, ArrayList> {

    @IntDef({NONE, EVENTS, PLACES, SAVED_EVENTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    public static final int NONE = -1;
    public static final int EVENTS = 0;
    public static final int PLACES = 1;
    public static final int SAVED_EVENTS = 2;

    public static final int NO_EVENT_ID = -1;

    public static final int FETCH_FROM_SERVER = 3;
    public static final int DO_NOT_FETCH_FROM_SERVER = 4;

    public static final String NO_FILTER_STRING = "";

    private ObserverFragment observerFragment;

    @Category
    private int what;
    private int fetchFromServer;

    public FetchAsyncTask(ObserverFragment observerFragment, @Category int what, int fetchFromServer) {
        this.observerFragment = observerFragment;
        this.what = what;
        this.fetchFromServer = fetchFromServer;
    }

    @Override
    protected ArrayList doInBackground(String... filter) {

        if (what == EVENTS) {
            if (fetchFromServer == FETCH_FROM_SERVER &&
                    NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
                fetchEventsFromServer();
            }
            if (filter != null && filter.length > 0) {
                switch (filter.length) {
                    case 1:
                        return EventsDatabase.getInstance().getEvents(filter[0]);
                    case 2:
                        return EventsDatabase.getInstance().getEvents(filter[0], filter[1]);
                }
            } else {
                return EventsDatabase.getInstance().getEvents();
            }
        } else if (what == PLACES) {
//            if (fetchFromServer == FETCH_FROM_SERVER &&
//                    NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
//                    fetchPlacesFromServer();
//            }
            if ((filter != null && filter.length > 0)) {
                return EventsDatabase.getInstance().getPlaces(filter[0]);
            } else {
                return EventsDatabase.getInstance().getPlaces();
            }
        } else if (what == SAVED_EVENTS) {
            if (filter != null && filter.length > 0) {
                return EventsDatabase.getInstance().getSavedEvents(filter[0]);
            } else {
                return EventsDatabase.getInstance().getSavedEvents();
            }
        }

        return null;
    }

    protected void onPostExecute(ArrayList result) {
        observerFragment.update(null, result);
    }

    private void fetchEventsFromServer() {
        HashMap<String, Object> params = new HashMap();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
        ServiceEvento.getInstance().callServiceMethod(params);
    }

    private void fetchPlacesFromServer() {
        // TODO exactly!
    }
}