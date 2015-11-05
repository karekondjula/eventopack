package com.evento.team2.eventspack.provider;

import android.os.AsyncTask;
import android.support.annotation.IntDef;

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

    @IntDef({EVENTS, PLACES, SAVED_EVENTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {}

    public static final int EVENTS = 0;
    public static final int PLACES = 1;
    public static final int SAVED_EVENTS = 2;

    public static final String FILTER_NAME = "0";
    public static final String FILTER_DATE = "1";

    public static final int FETCH_FROM_SERVER = 3;
    public static final int DO_NOT_FETCH_FROM_SERVER = 4;

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
            if (filter != null && filter.length > 0) {
                if (filter[0].equals(FILTER_NAME)) {
                    return EventsDatabase.getInstance().getEvents(filter[1]);
                } else if (filter[0].equals(FILTER_DATE)) {
                    return EventsDatabase.getInstance().getEvents(filter[1]);
                }
            } else {
                if (fetchFromServer == FETCH_FROM_SERVER &&
                        NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
                    fetchEventsFromServer();
                }
                return EventsDatabase.getInstance().getEvents(null);
            }
        } else if (what == PLACES) {
            if ((filter != null && filter.length > 0)) {

            } else {
                if (fetchFromServer == FETCH_FROM_SERVER &&
                        NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
                    fetchPlacesFromServer();
                } else {

                }
            }
        } else if (what == SAVED_EVENTS) {
            if (filter != null && filter.length > 0) {
                if (filter[0].equals(FILTER_NAME)) {
                    return EventsDatabase.getInstance().getSavedEvents(filter[1]);
                } else if (filter[0].equals(FILTER_DATE)) {
                    return EventsDatabase.getInstance().getSavedEvents(filter[1]);
                }
            } else {
                return EventsDatabase.getInstance().getSavedEvents(null);
            }
        }

        return null;
    }

    protected void onPostExecute(ArrayList events) {
        observerFragment.update(null, events);
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