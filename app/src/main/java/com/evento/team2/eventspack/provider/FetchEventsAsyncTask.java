package com.evento.team2.eventspack.provider;

import android.os.AsyncTask;

import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class FetchEventsAsyncTask extends AsyncTask<String, Void, ArrayList<Event>> {

    private ObserverFragment observerFragment;

    public FetchEventsAsyncTask(ObserverFragment observerFragment) {
        this.observerFragment = observerFragment;
    }

    @Override
    protected ArrayList<Event> doInBackground(String... filter) {

        if ((filter != null && filter.length == 1)) {
            return EventsDatabase.getInstance().getSavedEvents(filter[0]);
        } else {
            if (NetworkUtils.getInstance().isNetworkAvailable(observerFragment.getActivity())) {
                fetchEventsFromServer();
            }
            return EventsDatabase.getInstance().getSavedEvents(null);
        }
    }

    protected void onPostExecute(ArrayList<Event> events) {
            observerFragment.update(null, events);
    }

    private void fetchEventsFromServer() {
        HashMap<String, Object> params = new HashMap();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
        ServiceEvento.getInstance().callServiceMethod(params);
    }
}