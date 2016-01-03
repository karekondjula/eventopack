package com.evento.team2.eventspack.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.adapter.EventsRecyclerViewAdapter;
import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.soapservice.ServiceEvento;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends ObserverFragment {

    // TODO refactor ... hopefully dagger ^_^
    private static String SHARED_PREFERENCE_NAME = "eventi_preference";
    private static String SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS = "last_update_of_events";

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;
    @Bind(R.id.empty_view)
    TextView emptyAdapterTextView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsRecyclerViewAdapter eventsAdapter;
    private String lastFilterInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

            ButterKnife.bind(this, swipeRefreshLayout);

            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchEventsFromServer();
            });
        }

        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (eventsAdapter == null) {
            eventsAdapter = new EventsRecyclerViewAdapter(EventiApplication.applicationContext);
        }
        eventsRecyclerView.setAdapter(eventsAdapter);

        showLastUpdatedInfo();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (eventsAdapter != null) {
            if (eventsAdapter.getItemCount() == 0) {
                emptyAdapterTextView.setVisibility(View.VISIBLE);
            } else {
                emptyAdapterTextView.setVisibility(View.GONE);
            }
        }

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String lastUpdateDate = preferences.getString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, "");
        Date today = new Date();
        String todayDate = DateFormatterUtils.compareDateFormat.format(today);
        if (!todayDate.equals(lastUpdateDate)) {
            // get new events from server
            fetchEventsFromServer();
            preferences.edit().putString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, todayDate).apply();
        } else {
            // just load current events from database
            filterList(FetchAsyncTask.NO_FILTER_STRING);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            swipeRefreshLayout.removeAllViews();
            swipeRefreshLayout = null;
        }
    }

    @Override
    public void filterList(final String filter) {
        lastFilterInput = filter;

        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventsArrayList;

                if (!TextUtils.isEmpty(filter)) {
                    eventsArrayList = EventsDatabase.getInstance().getEvents(filter, String.valueOf(new Date().getTime()));
                } else {
                    eventsArrayList = EventsDatabase.getInstance().getEvents(FetchAsyncTask.NO_FILTER_STRING, String.valueOf(new Date().getTime()));
                }

                if (eventsAdapter != null) {
                    eventsAdapter.addEvents(eventsArrayList);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            eventsAdapter.notifyDataSetChanged();

                            if (eventsAdapter.getItemCount() > 0) {
                                if (emptyAdapterTextView != null) {
                                    emptyAdapterTextView.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public static FragmentEvents newInstance() {
        FragmentEvents fragmentEvents = new FragmentEvents();
        return fragmentEvents;
    }

    private void fetchEventsFromServer() {
        new Thread() {
            @Override
            public void run() {
                if (NetworkUtils.getInstance().isNetworkAvailable(EventiApplication.applicationContext)) {
                    HashMap<String, Object> params = new HashMap();
                    params.put(ServiceEvento.METHOD_NAME_KEY, ServiceEvento.METHOD_GET_ALL_EVENTS);
                    ServiceEvento.getInstance().callServiceMethod(params);

                    filterList(lastFilterInput);
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Snackbar.make(eventsRecyclerView,
                                    R.string.no_internet_connection_cached_events,
                                    Snackbar.LENGTH_LONG)
                                    .show();
                        });
                    }
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        }.start();
    }

    public void showLastUpdatedInfo() {

        SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String lastUpdateDate = preferences.getString(SHARED_PREFERENCE_LAST_UPDATE_OF_EVENTS, "");

        try {
            long lastUpdateTimestamp = DateFormatterUtils.compareDateFormat.parse(lastUpdateDate).getTime();
            lastUpdateDate = DateFormatterUtils.fullDateFormat.format(new Date(lastUpdateTimestamp));

            Snackbar.make(eventsRecyclerView,
                    "Last updated: " + lastUpdateDate,
                    Snackbar.LENGTH_SHORT)
                    .show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}