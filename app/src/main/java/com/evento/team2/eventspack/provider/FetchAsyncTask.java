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

    @IntDef({EVENTS, PLACES, SAVED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Update {}

    public static final int EVENTS = 0;
    public static final int PLACES = 1;
    public static final int SAVED = 2;

    public static final int FETCH_FROM_SERVER = 3;
    public static final int DO_NOT_FETCH_FROM_SERVER = 4;

    private ObserverFragment observerFragment;

    @Update
    private int what;
    private int fetchFromServer;

    public FetchAsyncTask(ObserverFragment observerFragment, @Update int what, int fetchFromServer) {
        this.observerFragment = observerFragment;
        this.what = what;
        this.fetchFromServer = fetchFromServer;
    }

    @Override
    protected ArrayList doInBackground(String... filter) {

        if (what == EVENTS) {
            if ((filter != null && filter.length == 1)) {
                return EventsDatabase.getInstance().getEvents(filter[0]);
            } else {
                if (fetchFromServer == FETCH_FROM_SERVER &&
                        NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
                    fetchEventsFromServer();
                }
                return EventsDatabase.getInstance().getEvents(null);
            }
        } else if (what == PLACES) {

        } else if (what == SAVED) {

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
}