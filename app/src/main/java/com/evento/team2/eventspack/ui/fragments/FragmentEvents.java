package com.evento.team2.eventspack.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.joanzapata.iconify.Iconify;
//import com.joanzapata.iconify.fonts.IoniconsModule;
//import rx.Observable;
//import rx.Subscription;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends Fragment implements Observer {

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsRecyclerViewAdapter eventsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);
        ButterKnife.bind(this, swipeRefreshLayout);
//        Iconify.with(new IoniconsModule());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new FetchEventsAsyncTask().execute();
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsAdapter = new EventsRecyclerViewAdapter(getActivity());
        eventsRecyclerView.setAdapter(eventsAdapter);

        new FetchEventsAsyncTask().execute();

        return swipeRefreshLayout;
    }

    private void fetchEventsFromServer() {
        HashMap<String, Object> params = new HashMap();
        params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
        ServiceEvento.getInstance().callServiceMethod(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        ServiceEvento.getInstance().addObserver(this);
    }

    @Override
    public void onPause() {
        ServiceEvento.getInstance().deleteObserver(this);
        super.onPause();
    }

    SearchView searchView = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterEvents(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterEvents(newText);
                    return true;
                }
            });
            // TODO daniel how to collapse the fucking search fucking bar!!!!!!!!!
            searchView.setOnQueryTextFocusChangeListener((view, queryTextFocused) -> {
                if (!queryTextFocused) {
//                    searchItem.collapseActionView();
//                    MenuItemCompat.collapseActionView(searchItem);
                }
            });
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryRefinementEnabled(true);
            searchView.setSubmitButtonEnabled(false);
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public static FragmentEvents newInstance() {
        FragmentEvents fragmentEvents = new FragmentEvents();
        return fragmentEvents;
    }

    @Override
    public void update(Observable observable, Object eventsArrayList) {

        if (eventsArrayList instanceof ArrayList) {
            eventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
            eventsAdapter.notifyDataSetChanged();
        }
    }

    public void filterEvents(String filter) {
        new FetchEventsAsyncTask().execute(filter);
    }

    private class FetchEventsAsyncTask extends AsyncTask<String, Void, ArrayList<Event>> {

        private boolean isPerformingFilter = false;

        @Override
        protected ArrayList<Event> doInBackground(String... filter) {

            if ((filter != null && filter.length == 1)) {
                isPerformingFilter = true;
                return EventsDatabase.getInstance().getSavedEvents(filter[0]);
            } else if (!NetworkUtils.getInstance().isNetworkAvailable(getActivity())) {
                return EventsDatabase.getInstance().getSavedEvents(null);
            } else {
                fetchEventsFromServer();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Event> events) {
            if (events == null) {
                // events are coming through the observer from ServiceEvento
            } else {
                // the events are fetched from the database and we cal manually Observer->update()
                update(null, events);

                if (!isPerformingFilter) {
                    Snackbar.make(getActivity().getCurrentFocus(),
                            "No internet connection. Showing cached events...",
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}


//    Subscription newEventsSubscription;
//
//    private Subscription subscribeOnEventsObservable() {
//        Observable<Event> eventsObservable = EventsObservable.getObservableBackgroundThread();
//
//        return eventsObservable.subscribe(sub -> eventsAdapter.addEvent(sub),
//                err -> {},
//                () -> {
//                    eventsAdapter = new EventsRecyclerViewAdapter(getActivity(), eventsAdapter.getEventsList());
//                    eventsRecyclerView.setAdapter(eventsAdapter);
//                });
//    }
