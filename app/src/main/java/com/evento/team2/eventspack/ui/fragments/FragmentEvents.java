package com.evento.team2.eventspack.ui.fragments;

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
import com.evento.team2.eventspack.provider.FetchAsyncTask;
import com.evento.team2.eventspack.ui.interfaces.ObserverFragment;
import com.evento.team2.eventspack.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daniel on 31-Jul-15.
 */
public class FragmentEvents extends ObserverFragment {

    @Bind(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;
    @Bind(R.id.empty_view)
    TextView emptyAdapterTextView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsRecyclerViewAdapter eventsAdapter;
    private String lastFilterInput;

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

        filterList(FetchAsyncTask.NO_FILTER_STRING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events_list, container, false);

            ButterKnife.bind(this, swipeRefreshLayout);

            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.EVENTS, FetchAsyncTask.FETCH_FROM_SERVER);
                if (TextUtils.isEmpty(lastFilterInput)) {
                    fetchAsyncTask.execute("", String.valueOf(new Date().getTime()));
                } else {
                    fetchAsyncTask.execute(lastFilterInput, String.valueOf(new Date().getTime()));
                }
            });
        }

        return swipeRefreshLayout;
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
    }

    public static FragmentEvents newInstance() {
        FragmentEvents fragmentEvents = new FragmentEvents();
        return fragmentEvents;
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
    public void update(Observable observable, Object eventsArrayList) {

        if (eventsArrayList instanceof ArrayList) {
            if (eventsAdapter != null) {
                // TODO ugly solution for a problem which is caused because I use
                // TODO one fetchasync task for all data fetching
                eventsAdapter.addEvents((ArrayList<Event>) eventsArrayList);
                eventsAdapter.notifyDataSetChanged();

                if (!NetworkUtils.getInstance().isNetworkAvailable(EventiApplication.applicationContext)) {
                    getActivity().runOnUiThread(() -> {
                        Snackbar.make(eventsRecyclerView,
                                R.string.no_internet_connection_cached_events,
                                Snackbar.LENGTH_LONG)
                                .show();
                    });
                }

                if (eventsAdapter.getItemCount() > 0) {
                    getActivity().runOnUiThread(() -> {
                        if (emptyAdapterTextView != null) {
                            emptyAdapterTextView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void filterList(String filter) {
        lastFilterInput = filter;
        fetchAsyncTask = new FetchAsyncTask(this, FetchAsyncTask.EVENTS, FetchAsyncTask.DO_NOT_FETCH_FROM_SERVER);
        if (TextUtils.isEmpty(filter)) {
            fetchAsyncTask.execute("", String.valueOf(new Date().getTime()));
        } else {
            fetchAsyncTask.execute(filter, String.valueOf(new Date().getTime()));
        }
    }
}