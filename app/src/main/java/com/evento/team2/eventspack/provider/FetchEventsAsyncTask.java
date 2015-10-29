package com.evento.team2.eventspack.provider;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class FetchEventsAsyncTask extends AsyncTask<String, Void, ArrayList<Event>> {

    private final Observable observable = new Observable();

    private boolean isPerformingFilter = false;
    private Activity activity;

    public FetchEventsAsyncTask(Activity activity, ObserverFragment observerFragment) {
        this.activity = activity;
        observable.addObserver(observerFragment);
    }

    @Override
    protected ArrayList<Event> doInBackground(String... filter) {

        if ((filter != null && filter.length == 1)) {
            isPerformingFilter = true;
            return EventsDatabase.getInstance().getSavedEvents(filter[0]);
        } else if (!NetworkUtils.getInstance().isNetworkAvailable(activity)) {
            return EventsDatabase.getInstance().getSavedEvents(null);
        } else {
            fetchEventsFromServer();
            return null;
        }
    }

    protected void onPostExecute(ArrayList<Event> events) {
        if (events == null) {
            // events are coming through the observer from ServiceEvento
            // our job ends here
            return;
        } else {
            // the events are fetched from the database and we cal manually Observer->update()
            observable.hasChanged();
            observable.notifyObservers(events);
            if (!isPerformingFilter) {
                Snackbar.make(activity.getCurrentFocus(),
                        "No internet connection. Showing cached events...",
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void fetchEventsFromServer() {
        HashMap<String, Object> params = new HashMap();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
        ServiceEvento.getInstance().callServiceMethod(params);
    }
}